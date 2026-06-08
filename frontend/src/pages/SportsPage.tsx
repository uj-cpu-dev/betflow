import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import MatchCard from '../components/MatchCard';
import BetSlip from '../components/BetSlip';

const SPORTS = [
  { key: 'cricket_t20_blast', label: 'T20 Blast' },
  { key: 'baseball_mlb', label: 'MLB' },
  { key: 'basketball_nba', label: 'NBA' },
];

async function fetchMatches(sport: string) {
  const res = await fetch(`/api/odds/matches/${sport}`);
  if (!res.ok) throw new Error('Failed to fetch matches');
  return res.json();
}

interface BetSelection {
  matchId: string;
  homeTeam: string;
  awayTeam: string;
  outcomeName: string;
  odds: number;
}

export default function SportsPage() {
  const [betSelection, setBetSelection] = useState<BetSelection | null>(null);

  return (
    <>
      <div className={`max-w-4xl mx-auto p-6 transition-all ${betSelection ? 'mr-80' : ''}`}>
        <h1 className="text-2xl font-bold mb-6">Live Odds</h1>
        {SPORTS.map(sport => (
          <SportSection
            key={sport.key}
            sportKey={sport.key}
            label={sport.label}
            onSelectOdds={setBetSelection}
          />
        ))}
      </div>
      <BetSlip selection={betSelection} onClose={() => setBetSelection(null)} />
    </>
  );
}

function SportSection({
  sportKey,
  label,
  onSelectOdds,
}: {
  sportKey: string;
  label: string;
  onSelectOdds: (selection: BetSelection) => void;
}) {
  const { data, isLoading, isError } = useQuery({
    queryKey: ['matches', sportKey],
    queryFn: () => fetchMatches(sportKey),
    refetchInterval: 5 * 60 * 1000,
  });

  if (isLoading) return <p className="text-gray-500 mb-4">Loading {label}...</p>;
  if (isError) return <p className="text-red-500 mb-4">Failed to load {label}</p>;
  if (!data?.length) return <p className="text-gray-500 mb-4">No matches for {label}</p>;

  return (
    <section className="mb-8">
      <h2 className="text-lg font-semibold mb-3 text-gray-700">{label}</h2>
      <div className="space-y-3">
        {data.map((match: any) => (
          <MatchCard key={match.id} match={match} onSelectOdds={onSelectOdds} />
        ))}
      </div>
    </section>
  );
}
