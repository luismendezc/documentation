CREATE TABLE IF NOT EXISTS stores (
    store_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS sales (
    sale_id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    product_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO stores (name, location) VALUES
('Candy Shop', '123 Sweet St'),
('Cake House', '456 Dessert Dr'),
('Choco World', '789 Cocoa Ln');

