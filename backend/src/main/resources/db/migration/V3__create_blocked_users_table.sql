CREATE TABLE blocked_users (
    id          UUID PRIMARY KEY,
    blocker_id  UUID NOT NULL,
    blocked_id  UUID NOT NULL,
    created_at  DATE NOT NULL,

    CONSTRAINT fk_blocked_blocker
        FOREIGN KEY (blocker_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_blocked_blocked
        FOREIGN KEY (blocked_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT uq_blocked_pair UNIQUE (blocker_id, blocked_id),
    CONSTRAINT chk_blocked_not_self CHECK (blocker_id <> blocked_id)
);

CREATE INDEX idx_blocked_blocker ON blocked_users (blocker_id);
CREATE INDEX idx_blocked_blocked ON blocked_users (blocked_id);

-- this drops BLOCKED from the friendship status check, since it's no longer a valid value
ALTER TABLE friendships DROP CONSTRAINT chk_friendship_status;
ALTER TABLE friendships ADD CONSTRAINT chk_friendship_status
    CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED'));