package we.ytc.disbordissimo.cli;

import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.client.exceptions.UnreachableServerException;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import static we.ytc.disbordissimo.cli.MainCli.*;

class InGuildCli {
    protected static void inGuild() {
        boolean isInGuildRunning = true;

        while (isInGuildRunning) {
            System.out.print(PROMPT);
            String cmd = sc.nextLine().strip().toLowerCase();

            switch (cmd) {
                case "owner":
                    try {
                        guildOwner();
                    } catch (CommandFailedException e) {
                        defaultErrHandling(e.getErrCode());

                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "ls chan":
                case "ls channels":
                case "lis chan":
                case "lis channels":
                case "list chan":
                case "list channels":
                    try {
                        listGuildChannels();
                    } catch (CommandFailedException e) {
                        defaultErrHandling(e.getErrCode());

                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;
                    
                case "create channel":
                case "make channel":
                    try {
                        createChannel();
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.CHANNEL_ALREADY_EXISTS) {
                            printErr("Err 1202: Channel already exists");
                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "drop channel":
                case "delete channel":
                    try {
                        dropChannel();
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.CHANNEL_NOT_FOUND) {
                            printErr("Err 1201: Channel not found");
                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "join channel":
                    try {
                        InChannelCli.inChannel(joinChannel());
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.CHANNEL_NOT_FOUND) {
                            printErr("Err 1201: Channel not found");
                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "q":
                case "quit":
                case "exit":
                    isInGuildRunning = deselGuild();
                    break;

                case "ping":
                    ping();
                    break;
                    
                default:
                    if (!cmd.isBlank()) {
                        printErr("Err: Command Not Recognized or authorized");
                    }
                    continue;
            }
            System.out.println();
        }
    }

    private static String joinChannel() throws CommandFailedException {
        listGuildChannels();
        System.out.print("Select the channel to join: ");
        int chanIndex = sc.nextInt() - 1;
        sc.nextLine();
        String guildOfChan = client.getGuilds()[guild];

        client.joinChannel(client.getGuildChannels(guildOfChan)[chanIndex], guildOfChan);

        return client.getGuildChannels(guildOfChan)[chanIndex];
    }

    private static void guildOwner() throws CommandFailedException {
        System.out.println("----------- Guild Owner -----------");
        System.out.println(client.getGuildOwner(client.getGuilds()[guild]));
    }

    private static void dropChannel() throws CommandFailedException {
        System.out.println("----------- Delete Channel -----------");
        listGuildChannels();
        System.out.print("Select the channel to delete: ");
        int chanToDel = sc.nextInt() - 1;
        String chan = client.getGuildChannels(client.getGuilds()[guild])[chanToDel];
        sc.nextLine();

        System.out.print("Are you sure to delete " + chan + " (y/n): ");
        String sel = sc.nextLine();
        if (sel.equals("y")) {
            client.dropGuildChannel(chan, client.getGuilds()[guild]);

            System.out.println("Channel Deleted Successfully!");
        } else if (sel.equals("n")) {
            System.out.println("Cancelled the cancellation :)");

        } else {
            printErr("Couldn't recognize command. Assuming n...");
            return;
        }
    }

    private static boolean deselGuild() {
        guild = -1;
        PROMPT = user + "@disbordissimo> ";
        return false;
    }

    private static void listGuildChannels() throws CommandFailedException {
        System.out.println("----------- List Guild Channels -----------");
        String[] channels = client.getGuildChannels(client.getGuilds()[guild]);
        for (int i = 1; i <= channels.length; i++) {
            System.out.println(i + ") " + channels[i - 1]);
        }
    }

    private static void createChannel() throws CommandFailedException {
        System.out.println("----------- Create Guild Channel -----------");
        System.out.print("Insert a name for your channel: ");
        String chanName = sc.nextLine();
        String[] guilds = client.getGuilds();

        client.createGuildChannel(chanName, guilds[guild]);

        System.out.println("Successfully created guild channel: " + chanName);
    }
}
