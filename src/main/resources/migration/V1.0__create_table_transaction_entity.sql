CREATE TABLE transaction_entity
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id   VARCHAR(255)   NOT NULL,
    transaction_type ENUM('PAYMENT', 'REFUND')    NOT NULL,
    amount           DECIMAL(19, 7) NOT NULL,
    currency         VARCHAR(10)    NOT NULL,
    result           ENUM('SUCCESS', 'FAILURE', 'DECLINED')   NOT NULL,
    created          TIMESTAMP      NOT NULL,
    last_updated     TIMESTAMP      NOT NULL
);

CREATE INDEX idx_transaction_id ON transaction_entity (transaction_id);