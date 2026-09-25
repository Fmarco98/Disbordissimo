# CreateGuildCommand:

## Request:

```
{
  "cmd" : "create-guild",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# CreateGuildChannelCommand:

## Request:

```
{
  "cmd" : "create-guild-channel",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "channel" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# DropGuildCommand:

## Request:

```
{
  "cmd" : "drop-guild",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# DropGuildChannelCommand:

## Request:

```
{
  "cmd" : "drop-guild-channel",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "channel" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# GetGuildChannelConnectedMembersCommand:

## Request:

```
{
  "cmd" : "get-guild-channel-connected-members",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "channel" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "connected-members" : [ /*opt*/
    "<member-1>",
    "<member-2>",
    ... ,
    "<member-K>"
  ],
  "transaction": <txID>
}
```

# GetGuildChannelsCommand:

## Request:

```
{
  "cmd" : "get-guild-channel",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "channels" : [ /*opt*/
    "<channel-1>",
    "<channel-2>",
    ... ,
    "<channel-K>"
  ],
  "transaction": <txID>
}
```

# GetGuildMembersCommand:

## Request:

```
{
  "cmd" : "get-guild-member",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "members" : [ /*opt*/
    "<member-1>",
    "<member-2>",
    ... ,
    "<member-K>"
  ],
  "transaction": <txID>
}
```

# GetGuildOwnerCommand:

## Request:

```
{
  "cmd" : "get-owner",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "owner" : "<member-owner>", /*opt*/
  "transaction": <txID>
}
```

# GetGuildsCommand:

## Request:

```
{
  "cmd" : "get-guilds",
  "userID" : <user-ID>,
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "guilds" : [ /*opt*/
    "<guild-1>",
    "<guild-2>",
    ... ,
    "<guild-K>"
  ],
  "transaction": <txID>
}
```

# JoinChannelCommand:

## Request:

```
{
  "cmd" : "join",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "channel" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "roomID" : <room-ID>, /*opt*/
  "roomPin" : "<room-pin>", /*opt*/
  "janus" : "<janus-URL-signaling-server>", /*opt | *** */
  "stun" : "<stun-url>", /*opt*/
  "transaction": <txID>
}
```

# JoinGuildCommand:

## Request:

```
{
  "cmd" : "join-guild",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# LeaveGuildCommand:

## Request:

```
{
  "cmd" : "leave-guild",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# LoginCommand:

## Request:

```
{
  "cmd" : "login",
  "username" : "<username>",
  "hpwd" : "<hpwd>", /* sha3-256 */
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "userID" : <user-ID>, /* opt */
  "transaction": <txID>
}
```

# PingCommand:

## Request:

```
{
  "cmd" : "ping",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# QuitChannelCommand:

## Request:

```
{
  "cmd" : "quit",
  "userID" : <user-ID>,
  "guild" : "<name>",
  "channel" : "<name>",
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# SignUpCommand:

## Request:

```
{
  "cmd" : "sign-up",
  "username" : "<username>",
  "hpwd" : "<hpwd>", /* sha3-256 */
  "transaction": <txID>
}
```

## Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "transaction": <txID>
}
```

# To be added

> ![IMPORTANT]
>
> This is the API syntax for the janus signaling tunnel, which is not currently implemented.

## Janus pull

### Request

```
{
  "cmd" : "janus",
  "userID" : <user-ID>,
  "janus": {...}, /* janus API request */ 
  "transaction": <txID>
}
```

### Response:

```
{
  "code" : <ResultCode>,
  "msgCode" : "<ResultMsg>",
  "janus" : {...} /* opt | janus API response */
  "transaction": <txID>
}
```

## janus push

```
{
  "code": <ResultCode>, /* janus push */
  "msgCode" : "<ResultMsg>", /* janus push */ 
  "janus" : {...} /* janus API response */
}
```