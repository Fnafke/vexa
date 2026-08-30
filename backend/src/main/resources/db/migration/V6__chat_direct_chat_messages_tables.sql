CREATE TABLE chats (
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE direct_chats (
    id            UUID PRIMARY KEY REFERENCES chats (id) ON DELETE CASCADE,
    user_one_id   UUID NOT NULL,
    user_two_id   UUID NOT NULL,

    CONSTRAINT fk_direct_chat_user_one
        FOREIGN KEY (user_one_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_direct_chat_user_two
        FOREIGN KEY (user_two_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT chk_direct_chat_not_self CHECK (user_one_id <> user_two_id),
    -- Enforces a consistent ordering (smaller UUID always stored as user_one_id),
    -- so the UNIQUE constraint below actually catches duplicate pairs without
    -- needing an OR-query, as discussed. Your service layer must respect this
    -- ordering when creating a DirectChat.
    CONSTRAINT chk_direct_chat_ordered CHECK (user_one_id < user_two_id),
    CONSTRAINT uq_direct_chat_pair UNIQUE (user_one_id, user_two_id)
);

CREATE INDEX idx_direct_chat_user_one ON direct_chats (user_one_id);
CREATE INDEX idx_direct_chat_user_two ON direct_chats (user_two_id);

CREATE TABLE messages (
    id          UUID PRIMARY KEY,
    sender_id   UUID NOT NULL,
    chat_id     UUID NOT NULL,
    content     TEXT NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_message_sender
        FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_message_chat
        FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE
);

CREATE INDEX idx_message_chat_created ON messages (chat_id, created_at);
CREATE INDEX idx_message_sender ON messages (sender_id);