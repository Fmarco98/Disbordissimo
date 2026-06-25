CREATE DATABASE disbordissimo;

START TRANSACTION;

CREATE TABLE db_info(
	version VARCHAR(10) NOT NULL,
    creation_date DATETIME NOT NULL DEFAULT now(),
    last_update DATETIME NOT NULL DEFAULT now(),
    project VARCHAR(30) NOT NULL
);

CREATE TABLE Users(
	id_user BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    passwd VARCHAR(32) NOT NULL,
    create_datetime DATETIME NOT NULL DEFAULT now(),
    delete_datetime DATETIME DEFAULT NULL
);

CREATE TABLE Guilds(
	id_guild BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,

    fk_owner BIGINT NOT NULL,
    FOREIGN KEY(fk_owner) REFERENCES Users(id_user)
);

CREATE TABLE Users_Guilds(
	id_users_guilds BIGINT PRIMARY KEY AUTO_INCREMENT,

    fk_user BIGINT NOT NULL,
    fk_guild BIGINT NOT NULL,

    FOREIGN KEY (fk_user) REFERENCES Users(id_user),
    FOREIGN KEY (fK_guild) REFERENCES Guilds(id_guild)
);

CREATE TABLE Channels(
	id_channel BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,

    fk_guild BIGINT NOT NULL,

    FOREIGN KEY(fk_guild) REFERENCES Guilds(id_guild)
);

COMMIT;
