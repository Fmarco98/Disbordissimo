# Disbordissimo

A voice chat application.
 
| Project info |                                                                                                                                                                        |
|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| License      | ***[GPLv3](https://github.com/Fmarco98/Disbordissimo/blob/main/LICENSE)***                                                                                             |
| Version      | ***1.0-alpha***                                                                                                                                                        | 
| Status       | ***Paused*** <!-- WIP -->                                                                                                                                              |
| Used tools   | [Janus WebRTC Server](https://janus.conf.meetecho.com/)<br> [Gson](https://github.com/google/gson)<br> [mysql-connector-j](https://github.com/mysql/mysql-connector-j) |

***

## Disbordissimo Server [WIP]
The Disbordissimo server code is contained in the `server` package.

### Installation guide

To run the server, you need a [Janus WebRTC Server](https://janus.conf.meetecho.com/) and a [Mysql](https://www.mysql.com/) Server.

> [!NOTE]
> Any "type" of SQL Server can be used, but some queries might change depending on the chosen "type".
>
> Example:
> ```
> Standard SQL: BEGIN TRANSACTION;
> MySQL: START TRANSACTION;
> ```

1. **DB Setup** <br> Install a MySQL server, then use the [db.sql](https://github.com/Fmarco98/Disbordissimo/blob/main/SQL_DB/db.sql) 
file to import the DB.

> [!TIP]
> A dedicated DB user can be created; that should have the permission to use [DML]() and [DQL]() queries only.

2. **Janus setup** <br> To install Janus, see: [Janus Github-repo](https://github.com/meetecho/janus-gateway/blob/master/README.md).

> [!TIP]
> Delete all permanent rooms, which are in the config by default, to avoid potential Room ID conflicts.

3. **Install DisbordissimoServer** <br> Install the server, then run it to autogen the config file (located at `*/**/.config/`).

## Disbordissimo Client [API] [WIP]
A simple API documentation can be found in [API_syntax.md](https://github.com/Fmarco98/Disbordissimo/blob/main/API-syntax.md).

The Disbordissimo client API wrapper code is contained in the `client` package.

> [!NOTE]
> You are free to communicate with Disbordissimo Server using the API Wrapper or "Pure" API.
> It's recommended to use the API wrapper. 

## Application Examples [WIP]
 - **CLI**: it's contained in the `cli` package.
 - **GUI**: coming soon <!-- or never -->

***

## Possible Improvements

- **Session-oriented communication**: it aims to reduce spoofing attacks, which are easier to perform on a user-ID 
oriented communication.

- **Encrypted communication**: Currently every message is sent as plain text, adopting any type of encryption will reduce 
the effect of sniffing attacks. 

- **Signaling tunnel**: Implementing a signaling filter tunnel allows control over the Janus API message-passing between 
janus and clients by blocking dangerous messages.

- **Janus API hiding**: Hide the Janus API from clients and force them to transmit WebRTC messages via DisbordissimoAPI.
This change makes it possible to use every WebRTC server that implements [MCU Architecture](https://www.red5.net/blog/webrtc-architecture-p2p-sfu-mcu-xdn/#multipoint-conferencing-unit-mcu) (because it's treated 
server-side).

> [!IMPORTANT]
> ## Disclaimer
> 
> This project is NOT intended to make any money and has been created for fun only.
> 
> Anyone is free to fork and continue the project.
