CREATE TABLE friendships (
    id            UUID PRIMARY KEY,
    requester_id  UUID NOT NULL,
    addressee_id  UUID NOT NULL,
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at    DATE NOT NULL,
    updated_at    DATE NOT NULL,

    CONSTRAINT fk_friendship_requester
        FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_friendship_addressee
        FOREIGN KEY (addressee_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT uq_friendship_pair UNIQUE (requester_id, addressee_id),
    CONSTRAINT chk_friendship_not_self CHECK (requester_id <> addressee_id),
    CONSTRAINT chk_friendship_status CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED', 'BLOCKED'))
);

CREATE INDEX idx_friendship_requester ON friendships (requester_id);
CREATE INDEX idx_friendship_addressee ON friendships (addressee_id);
CREATE INDEX idx_friendship_status ON friendships (status);