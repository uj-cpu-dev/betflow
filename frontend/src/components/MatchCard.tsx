interface Outcome {
  name: string;
  price: number;
}

interface Market {
  key: string;
  outcomes: Outcome[];
}

interface Bookmaker {
  key: string;
  title: string;
  markets: Market[];
}

interface Match {
  id: string;
  home_team: string;
  away_team: string;
  commence_time: string;
  bookmakers: Bookmaker[];
}

interface MatchCardProps {
  match: Match;
  onSelectOdds: (selection: {
    matchId: string;
    homeTeam: string;
    awayTeam: string;
    outcomeName: string;
    odds: number;
  }) => void;
}

export default function MatchCard({ match, onSelectOdds }: MatchCardProps) {
  // Pick the first bookmaker that has h2h market
  const bookmaker = match.bookmakers?.find(b =>
    b.markets?.some(m => m.key === 'h2h')
  );
  const outcomes = bookmaker?.markets?.find(m => m.key === 'h2h')?.outcomes ?? [];

  const matchTime = new Date(match.commence_time).toLocaleString('en-GB', {
    weekday: 'short', day: 'numeric', month: 'short',
    hour: '2-digit', minute: '2-digit'
  });

  return (
    <div className="border rounded-lg p-4 bg-white shadow-sm">
      <div className="flex justify-between items-start mb-3">
        <div>
          <p className="font-medium">{match.home_team} vs {match.away_team}</p>
          <p className="text-sm text-gray-500">{matchTime}</p>
        </div>
        {bookmaker && (
          <span className="text-xs text-gray-400">{bookmaker.title}</span>
        )}
      </div>

      {outcomes.length > 0 && (
        <div className="flex gap-2">
          {outcomes.map(outcome => (
            <button
              key={outcome.name}
              onClick={() => onSelectOdds({
                matchId: match.id,
                homeTeam: match.home_team,
                awayTeam: match.away_team,
                outcomeName: outcome.name,
                odds: outcome.price,
              })}
              className="flex-1 border rounded p-2 text-center hover:bg-blue-50 hover:border-blue-400 transition-colors"
            >
              <p className="text-xs text-gray-500 truncate">{outcome.name}</p>
              <p className="font-bold text-blue-600">{outcome.price}</p>
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
