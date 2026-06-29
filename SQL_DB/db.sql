START TRANSACTION;

CREATE DATABASE IF NOT EXISTS disbordissimo;
USE disbordissimo;

CREATE TABLE IF NOT EXISTS db_info(
	version VARCHAR(10) NOT NULL,
    creation_date DATETIME NOT NULL DEFAULT now(),
    last_update DATETIME NOT NULL DEFAULT now(),
    project VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
	id_user BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    passwd VARCHAR(256) NOT NULL,
    create_datetime DATETIME NOT NULL DEFAULT now(),
    delete_datetime DATETIME DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS guilds(
	id_guild BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,

    fk_owner BIGINT NOT NULL,
    FOREIGN KEY(fk_owner) REFERENCES users(id_user)
);

CREATE TABLE IF NOT EXISTS users_guilds(
	id_users_guilds BIGINT PRIMARY KEY AUTO_INCREMENT,

    fk_user BIGINT NOT NULL,
    fk_guild BIGINT NOT NULL,

    FOREIGN KEY (fk_user) REFERENCES users(id_user),
    FOREIGN KEY (fK_guild) REFERENCES guilds(id_guild)
);

CREATE TABLE IF NOT EXISTS channels(
	id_channel BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,

    fk_guild BIGINT NOT NULL,

    FOREIGN KEY(fk_guild) REFERENCES guilds(id_guild)
);

INSERT INTO db_info(version, project) VALUES
("1.0", "dibordissimo");

COMMIT;
