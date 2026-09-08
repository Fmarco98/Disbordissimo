# Disbordissimo

A voice chat application.
 
| Project info |  |
| ------- | ------ |
| License | ***[GPLv3](https://github.com/Fmarco98/Disbordissimo/blob/main/LICENSE)*** |
| Version | ***1.0-alpha***  | 
| status  | ***WIP*** |

***

## Disbordissimo Server [WIP]
The Disbordissimo server code is contained in `server` package.

## Disbordissimo Client [API] [WIP]
The Disbordissimo client code is contained in `client` package.

> [!NOTE]
> The user is free to communicate with Disbordissimo Server using the API Wrapper or "True" API.
> It's suggested to use the API wrapper (contained in `client` package). 

## Applications Examples [WIP]
 - **CLI**: it's contained in `cli` package.

***

## Possible Improvements

- **Session-oriented communication**: it aims to reduce spoofing attacks, which are easier to perform on a user-ID 
oriented communication.

- **Encrypted communication**: Currently every message is sent as plain text, adopting any type of encryption will reduce 
the effect of sniffing attacks. 

- **Signaling tunnel**: Implementing a signaling filter tunnel allows to hide the janus API to the clients and control 
the client-janus communication blocking dangerous messages.

> [!IMPORTANT]
> ## Disclaimer
> 
> This project is NOT intended to make any money and has been created for fun only.
> Everyone is free to fork and continue the project.
