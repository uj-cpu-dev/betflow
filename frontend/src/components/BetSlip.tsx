import { useState } from 'react';

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

  const potentialReturn = selection && stake
    ? (parseFloat(stake) * selection.odds).toFixed(2)
    : null;

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

      <button
        disabled={!stake || parseFloat(stake) <= 0}
        className="mt-auto w-full bg-blue-600 text-white py-3 rounded-lg font-semibold disabled:opacity-40 disabled:cursor-not-allowed hover:bg-blue-700 transition-colors"
      >
        Place Bet
      </button>
    </div>
  );
}