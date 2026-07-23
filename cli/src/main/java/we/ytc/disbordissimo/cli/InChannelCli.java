package we.ytc.disbordissimo.cli;

import we.ytc.disbordissimo.client.exceptions.CommandFailedException;

import javax.sound.sampled.LineUnavailableException;

import static we.ytc.disbordissimo.cli.MainCli.*;

class InChannelCli {
    /**
     * Method called by {@link InGuildCli} when a user joins a channel. Handles all audio communications and commands
     * that a logged user in a channel can do
     *
     * @param channelName the channel's name
     */
    protected static void inChannel(String channelName) {
        boolean isInChannel = true;

        System.out.println("----------- Joined Channel -----------");

        while (isInChannel) {
            System.out.println("Currently in channel: " + channelName);
            System.out.println("Type \"leave\" to leave the channel");

            String cmd = sc.nextLine();
            switch (cmd) {
                case "q":
                case "exit":
                case "quit":
                case "leave":
                    // Handles the exit from a channel and closes all audio sinks.
                    try {
                        client.quitChannel(channelName, client.getGuilds()[guild]);
                        isInChannel = false;
                    } catch (CommandFailedException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "users":
                    // Prints a list of users currently in the channel
                    try {
                        String[] users = client.getChannelConnectedMembers(channelName, client.getGuilds()[guild]);
                        System.out.println("----------- Users In Channel -----------");
                        for (int i = 1; i <= users.length; i++) {
                            System.out.println(i + ") " + users[i - 1]);
                        }

                    } catch (CommandFailedException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "ping":
                    ping();
                    break;

                case "help":
                    help();
                    break;

                default:
                    if (!cmd.isBlank()) {
                        printErr("Err: Command Not Recognized or not authorized");
                    }
                    continue;
            }
            System.out.println();

        }
    }

    private static void help() {
        System.out.println("----------- Help -----------");
        System.out.println("Available commands: ");
        System.out.println(" - users");
        System.out.println(" - leave (aliases: q, quit, exit)");
        System.out.println(" - ping");
        System.out.println(" - help");
        System.out.println("----------------------------");
    }
}
