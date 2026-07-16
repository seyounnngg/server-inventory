CREATE TABLE user(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    team    VARCHAR(50)
);

CREATE TABLE server(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    hostname    VARCHAR(100) NOT NULL,
    ip_address  VARCHAR(50),
    type        VARCHAR(20),
    owner_id    BIGINT,
    FOREIGN KEY (owner_id) REFERENCES user(id)
);

CREATE TABLE server_check(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    server_id   BIGINT NOT NULL,
    checked_at  DATETIME NOT NULL,
    status      VARCHAR(10),
    FOREIGN KEY (server_id) REFERENCES server(id)
);