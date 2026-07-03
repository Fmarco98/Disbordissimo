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
    passwd VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS guilds(
	id_guild BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,

    fk_owner BIGINT NOT NULL,
    FOREIGN KEY(fk_owner) REFERENCES users(id_user) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users_guilds(
	id_users_guilds BIGINT PRIMARY KEY AUTO_INCREMENT,

    fk_user BIGINT NOT NULL,
    fk_guild BIGINT NOT NULL,

    FOREIGN KEY (fk_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (fK_guild) REFERENCES guilds(id_guild) ON DELETE CASCADE,

    UNIQUE (fk_user, fk_guild)
);

CREATE TABLE IF NOT EXISTS channels(
	id_channel BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,

    fk_guild BIGINT NOT NULL,

    FOREIGN KEY(fk_guild) REFERENCES guilds(id_guild) ON DELETE CASCADE,
    UNIQUE(name, fk_guild)
);

INSERT INTO db_info(version, project) VALUES
("1.0", "disbordissimo");

CREATE VIEW channel_guild_byname AS (
    SELECT g.id_guild, c.id_channel, g.name as guildname, c.name as channelname
    FROM guilds g
    JOIN channels c ON g.id_guild = c.fk_guild
);

CREATE VIEW user_guild_byname AS (
    SELECT g.id_guild, g.name as guildname, u.id_user as id_member, u.username as member
    FROM guilds g
    JOIN users_guilds ug ON g.id_guild = ug.fk_guild
    JOIN users u ON u.id_user = ug.fk_user
);

COMMIT;
