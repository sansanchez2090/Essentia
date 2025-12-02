CREATE TABLE perfumes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    release_year INTEGER,
    gender VARCHAR(20),
    image_url VARCHAR(255)
);