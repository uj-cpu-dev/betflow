import { useState } from 'react';
import { useAuthStore } from '../store/authStore';

interface BetSelection {
  matchId: string;
  homeTeam: string;
  awayTeam: string;
  outcomeName: string;
  odds: number;
}

interface BetSlipProps {
  selection: BetSelection | null;
  onClose: () => void;
}

export default function BetSlip({ selection, onClose }: BetSlipProps) {
  const [stake, setStake] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const { user, token } = useAuthStore();

  function getUserIdFromToken(token: string): string | null {
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      return decoded.userId ?? null;
    } catch {
      return null;
    }
  }

  const potentialReturn = selection && stake
    ? (parseFloat(stake) * selection.odds).toFixed(2)
    : null;

  async function handlePlaceBet() {
    const userId = getUserIdFromToken(token!);
    if (!userId) {
      setError('Session expired. Please log in again.');
      return;
    }

    if (!selection || !stake) return;
    setLoading(true);
    setError(null);

    try {
      const res = await fetch('/api/bets', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          userId: userId,
          matchId: selection.matchId,
          homeTeam: selection.homeTeam,
          awayTeam: selection.awayTeam,
          outcomeName: selection.outcomeName,
          odds: selection.odds,
          stake: parseFloat(stake),
        }),
      });

      if (!res.ok) throw new Error('Failed to place bet');

      setSuccess(true);
      setStake('');
      setTimeout(() => {
        setSuccess(false);
        onClose();
      }, 2000);
    } catch (e) {
      setError('Failed to place bet. Please try again.');
    } finally {
      setLoading(false);
    }
  }

  if (!selection) return null;

  return (
    <div className="fixed right-0 top-0 h-full w-80 bg-white shadow-xl border-l p-6 flex flex-col z-50">
      <div className="flex justify-between items-center mb-6">
        <h2 className="font-bold text-lg">Bet Slip</h2>
        <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-xl">✕</button>
      </div>

      <div className="border rounded-lg p-4 mb-4 bg-gray-50">
        <p className="text-sm text-gray-500 mb-1">
          {selection.homeTeam} vs {selection.awayTeam}
        </p>
        <p className="font-medium">{selection.outcomeName}</p>
        <p className="text-blue-600 font-bold text-lg">{selection.odds}</p>
      </div>

      <div className="mb-4">
        <label className="block text-sm text-gray-600 mb-1">Stake (£)</label>
        <input
          type="number"
          min="0"
          placeholder="0.00"
          value={stake}
          onChange={e => setStake(e.target.value)}
          className="w-full border rounded p-2 text-lg focus:outline-none focus:border-blue-400"
        />
      </div>

      {potentialReturn && (
        <div className="flex justify-between text-sm mb-6">
          <span className="text-gray-500">Potential return</span>
          <span className="font-bold text-green-600">£{potentialReturn}</span>
        </div>
      )}

      {error && (
        <p className="text-red-500 text-sm mb-4">{error}</p>
      )}

      {success && (
        <p className="text-green-600 text-sm mb-4 font-medium">Bet placed successfully!</p>
      )}

      <button
        onClick={handlePlaceBet}
        disabled={!stake || parseFloat(stake) <= 0 || loading}
        className="mt-auto w-full bg-blue-600 text-white py-3 rounded-lg font-semibold disabled:opacity-40 disabled:cursor-not-allowed hover:bg-blue-700 transition-colors"
      >
        {loading ? 'Placing...' : 'Place Bet'}
      </button>
    </div>
  );
}
