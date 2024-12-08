

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    discount_percentage DOUBLE NOT NULL,
    rating DOUBLE NOT NULL,
    stock INT NOT NULL,
    brand VARCHAR(255),
    sku VARCHAR(255) NOT NULL UNIQUE,
    weight DOUBLE NOT NULL,
    warranty_information VARCHAR(255),
    shipping_information VARCHAR(255),
    availability_status VARCHAR(255),
    return_policy VARCHAR(255),
    minimum_order_quantity INT,
    thumbnail VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    barcode VARCHAR(255) NOT NULL UNIQUE,
    qr_code VARCHAR(255),
    width DOUBLE NOT NULL,
    height DOUBLE NOT NULL,
    depth DOUBLE NOT NULL
);


CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    rating DOUBLE NOT NULL,
    comment TEXT,
    date TIMESTAMP NOT NULL,
    reviewer_name VARCHAR(255) NOT NULL,
    reviewer_email VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS product_tags (
    product_id BIGINT NOT NULL,
    tag VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS product_images (
    product_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
