package we.ytc.disbordissimo.cli;

import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.client.exceptions.NotLoggedInException;
import we.ytc.disbordissimo.client.exceptions.UnreachableServerException;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import static we.ytc.disbordissimo.cli.MainCli.*;

class LoggedInCli {
    protected static boolean isLoggedInRunning;
    
    protected static void loggedIn() {
        isLoggedInRunning = true;

        while (isLoggedInRunning) {
            System.out.print(PROMPT);
            String cmd = sc.nextLine().strip().toLowerCase();

            switch (cmd) {
                case "ls guilds":
                case "lis guilds":
                case "list guilds":
                    try {
                        listGuilds();
                    } catch (CommandFailedException e) {
                        defaultErrHandling(e.getErrCode());

                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "use guild":
                case "sel guild":
                case "select guild":
                    try {
                        guild = selectGuild();
                        System.out.println();
                        InGuildCli.inGuild();
                        continue;
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.GUILD_NOT_FOUND) {
                            printErr("Err 1101: Guild not found");
                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }

                case "make guild":
                case "create guild":
                    try {
                        createGuild();
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.GUILD_ALREADY_EXISTS) {
                            printErr("Err 1102: Guild already exists");
                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "delete guild":
                case "drop guild":
                    try {
                        dropGuild();
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.GUILD_NOT_FOUND) {
                            printErr("Err 1101: Guild not found");
                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "join guild":
                    try {
                        joinGuild();
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.GUILD_NOT_FOUND) {
                            printErr("Err 1101: Guild not found");

                        } else if (e.getErrCode() == ReturnCodes.GUILD_ALREADY_JOINED) {
                            printErr("Err 1102: Already joined in guild");

                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "leave guild":
                    try {
                        leaveGuild();
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.GUILD_NOT_FOUND) {
                            printErr("Err 1101: Guild not found");
                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "logout":
                    client.logout();
                    System.out.println("Successfully logged out!");
                    isLoggedInRunning = false;
                    break;

                case "ping":
                    ping();
                    break;

                case "q":
                case "exit":
                case "quit":
                    printErr("First run the logout command");

                default:
                    if (!cmd.isBlank()) {
                        printErr("Err: Command Not Recognized or authorized");
                    }
                    continue;
            }
            System.out.println();
        }
    }

    private static void leaveGuild() throws CommandFailedException {
        System.out.println("----------- Leave Guild -----------");
        listGuilds();
        System.out.print("Select the guild to leave: ");
        int guildToLeaveIndex = sc.nextInt() - 1;
        sc.nextLine();
        String guildToLeave = client.getGuilds()[guildToLeaveIndex];

        client.leaveGuild(guildToLeave);

        System.out.println("Successfully left guild: " + guildToLeave);
    }

    private static void joinGuild() throws CommandFailedException {
        System.out.println("----------- Join Guild -----------");
        System.out.print("Insert the name of the guild to join to: ");
        String guildToJoin = sc.nextLine();

        client.joinGuild(guildToJoin);

        System.out.println("Successfully joined into " + guildToJoin);
    }

    private static void dropGuild() throws CommandFailedException {
        listGuilds();
        System.out.print("Select the guild to drop: ");
        int guildToDropIndex = sc.nextInt() - 1;
        sc.nextLine();

        String guildToDrop = client.getGuilds()[guildToDropIndex];
        System.out.print("Are you sure to delete " + guildToDrop + " (y/n): ");
        String sel = sc.nextLine();
        if (sel.equals("y")) {
            client.dropGuild(guildToDrop);

            System.out.println("Guild " + guildToDrop + " deleted Successfully!");
        } else if (sel.equals("n")) {
            System.out.println("Cancelled the cancellation :)");

        } else {
            printErr("Couldn't recognize command. Assuming n...");
            return;
        }
    }

    private static int selectGuild() throws CommandFailedException {
        listGuilds();
        System.out.print("Select the guild: ");
        int guild = sc.nextInt() - 1;
        sc.nextLine();
        PROMPT = user + "@disbordissimo(" + client.getGuilds()[guild] + ")> ";

        System.out.println("Successfully selected guild!");
        return guild;
    }

    private static void listGuilds() throws CommandFailedException {
        System.out.println("----------- Current Guilds -----------");
        String[] guilds = client.getGuilds();
        for (int i = 1; i <= guilds.length; i++) {
            System.out.println(i + ") " + guilds[i - 1]);
        }
    }

    private static void createGuild() throws CommandFailedException {
        System.out.println("----------- Create Guild -----------");
        System.out.print("Insert a name for your guild: ");
        String guildName = sc.nextLine();

        client.createGuild(guildName);

        System.out.println("Successfully created guild: " + guildName);
    }
}
