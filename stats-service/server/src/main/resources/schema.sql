CREATE TABLE IF NOT EXISTS hits (
    id SERIAL PRIMARY KEY,
    app VARCHAR(100) NOT NULL,
    uri VARCHAR(100) NOT NULL,
    ip VARCHAR(15) NOT NULL,
    timestamp timestamp
);