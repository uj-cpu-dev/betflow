CREATE TABLE settlements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bet_id UUID NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    outcome VARCHAR(10) NOT NULL,
    payout DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    settled_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_settlements_bet_id ON settlements(bet_id);
CREATE INDEX idx_settlements_user_id ON settlements(user_id);