-- CREATE TABLE roles
-- (
--     id          BIGSERIAL PRIMARY KEY,
--     name        VARCHAR(255) NOT NULL,
--     create_date TIMESTAMP    NOT NULL,
--     modify_date TIMESTAMP    NOT NULL
-- );
CREATE TABLE brands
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255) NOT NULL,
    image       VARCHAR(255),
    is_active   BOOLEAN      NOT NULL,
    create_date TIMESTAMP    NOT NULL,
    modify_date TIMESTAMP    NOT NULL
);

CREATE TABLE sales
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255) NOT NULL,
    discount    INT,
    is_active   BOOLEAN      NOT NULL,
    create_date TIMESTAMP    NOT NULL,
    modify_date TIMESTAMP    NOT NULL
);
CREATE TABLE accounts
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role_id     BIGINT,
    is_active   BOOLEAN      NOT NULL,
    create_date TIMESTAMP    NOT NULL,
    modify_date TIMESTAMP    NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);
CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY,
    code        VARCHAR(20)   NOT NULL UNIQUE,
    description VARCHAR(1000) NOT NULL,
    name        VARCHAR(50)   NOT NULL,
    view        BIGINT        NOT NULL,
    brand_id    BIGINT,
    sale_id     BIGINT,
    is_active   BOOLEAN       NOT NULL,
    create_date TIMESTAMP     NOT NULL,
    modify_date TIMESTAMP     NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brands (id),
    FOREIGN KEY (sale_id) REFERENCES sales (id)
);

CREATE TABLE account_detail
(
    id         BIGSERIAL PRIMARY KEY,
    birthdate  TIMESTAMP    NOT NULL,
    email      VARCHAR(50)  NOT NULL,
    fullname   VARCHAR(50)  NOT NULL,
    gender     VARCHAR(10)  NOT NULL,
    phone      VARCHAR(11)  NOT NULL,
    account_id BIGINT,
    address    VARCHAR(265) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);
CREATE TABLE order_status
(
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    create_date TIMESTAMP    NOT NULL,
    update_date TIMESTAMP    NOT NULL
);

CREATE TABLE vouchers
(
    id          BIGSERIAL PRIMARY KEY,
    code        VARCHAR(12) NOT NULL UNIQUE,
    count       INT,
    discount    INT,
    create_date TIMESTAMP,
    expire_date TIMESTAMP,
    is_active   BOOLEAN     NOT NULL
);
CREATE TABLE orders
(
    id              BIGSERIAL PRIMARY KEY,
    address         VARCHAR(255)     NOT NULL,
    fullname        VARCHAR(50)      NOT NULL,
    is_pending      BOOLEAN          NOT NULL,
    note            VARCHAR(1000),
    phone           VARCHAR(11)      NOT NULL,
    total           DOUBLE PRECISION NOT NULL,
    account_id      BIGINT,
    order_status_id BIGINT,
    email           VARCHAR(255)     NOT NULL,
    voucher_id      BIGINT,
    encode_url      VARCHAR(255),
    seen            BOOLEAN          NOT NULL,
    code            VARCHAR(1000),
    description     VARCHAR(1000),
    shipment        VARCHAR(1000),
    payment         VARCHAR(1000),
    ship_date       TIMESTAMP,
    create_date     TIMESTAMP        NOT NULL,
    modify_date     TIMESTAMP        NOT NULL,
    FOREIGN KEY (order_status_id) REFERENCES order_status (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id),
    FOREIGN KEY (voucher_id) REFERENCES vouchers (id)
);
CREATE TABLE attribute
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50)      NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    size        INT              NOT NULL,
    stock       INT              NOT NULL,
    product_id  BIGINT,
    cache       INT              NOT NULL,
    create_date TIMESTAMP        NOT NULL,
    modify_date TIMESTAMP        NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products (id)
);
CREATE TABLE cart_item
(
    id           BIGSERIAL PRIMARY KEY,
    quantity     INT,
    account_id   BIGINT,
    attribute_id BIGINT,
    last_price   DOUBLE PRECISION,
    is_active    BOOLEAN,
    FOREIGN KEY (account_id) REFERENCES accounts (id),
    FOREIGN KEY (attribute_id) REFERENCES attribute (id)
);
CREATE TABLE categories
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255) NOT NULL,
    is_active   BOOLEAN      NOT NULL,
    create_date TIMESTAMP    NOT NULL,
    modify_date TIMESTAMP    NOT NULL
);
CREATE TABLE images
(
    id          BIGSERIAL PRIMARY KEY,
    image_link  VARCHAR(255) NOT NULL,
    is_active   BOOLEAN      NOT NULL,
    name        VARCHAR(255) NOT NULL,
    product_id  BIGINT,
    create_date TIMESTAMP    NOT NULL,
    modify_date TIMESTAMP    NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products (id)
);
CREATE TABLE notifications
(
    id         BIGSERIAL PRIMARY KEY,
    content    VARCHAR(255),
    read       BOOLEAN,
    deliver    BOOLEAN,
    order_id   BIGINT,
    type       INT,
    product_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);
CREATE TABLE order_detail
(
    id           BIGSERIAL PRIMARY KEY,
    origin_price DOUBLE PRECISION NOT NULL,
    quantity     INT              NOT NULL,
    sell_price   DOUBLE PRECISION NOT NULL,
    attribute_id BIGINT,
    order_id     BIGINT,
    FOREIGN KEY (attribute_id) REFERENCES attribute (id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);


CREATE TABLE product_category
(
    id          BIGSERIAL PRIMARY KEY,
    category_id BIGINT,
    product_id  BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

