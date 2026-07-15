package we.ytc.disbordissimo.cli;

import we.ytc.disbordissimo.client.ClientFactory;
import we.ytc.disbordissimo.client.DisbordissimoClient;
import we.ytc.disbordissimo.client.exceptions.CommandFailedException;
import we.ytc.disbordissimo.client.exceptions.UnreachableServerException;
import we.ytc.disbordissimo.common.jsonio.ReturnCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class MainCli {
    protected static String PROMPT = "disbordissimo> ";
    protected static ClientFactory.Config config;
    protected static DisbordissimoClient client;
    protected static Scanner sc;
    protected static int guild;

    protected static String user;
    protected static boolean isMainRunning;

    protected static MicManager mm = new MicManager();
    protected static OutManager om = new OutManager();

    public static void main(String[] args) {
        sysErrRedirection();

        config = new ClientFactory.Config("localhost", 6969);
        client = ClientFactory.create(config);
        sc = new Scanner(System.in);
        guild = -1;

        isMainRunning = isServerRunning();

        client.setPacketReceivedHandler(bArr -> om.sound(bArr));
        client.setPacketSendingHandler(mm::getMicBytes);

        if (isMainRunning) {
            printGreeting();
        }

        while (isMainRunning) {
            System.out.print(PROMPT);
            String cmd = sc.nextLine().strip().toLowerCase();

            switch (cmd) {
                case "login":
                    try {
                        login();
                        System.out.println();
                        LoggedInCli.loggedIn();
                        continue;
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.USER_NOT_FOUND) {
                            printErr("Err 1001: Incorrect username or password");
                        } else {
                            defaultErrHandling(e.getErrCode());
                        }
                    } catch (UnreachableServerException ex) {
                        unreachbleServerHandling();
                    }
                    break;

                case "signup":
                    try {
                        signup();
                    } catch (CommandFailedException e) {
                        if (e.getErrCode() == ReturnCodes.USER_ALREADY_EXISTS) {
                            printErr("Err 1002: User Already Exists");
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
                    if (client.isLoggedIn()) {
                        client.logout();
                    }
                    client.destroy();

                    isMainRunning = false;

                    System.out.println("Goodbye!");
                    break;

                case "ping":
                    ping();
                    break;

                default:
                    if (!cmd.isBlank()) {
                        printErr("Err: Command Not Recognized or not authorized");
                    }
                    continue;
            }
            System.out.println();
        }

        client.destroy();
    }

    private static boolean isServerRunning() {
        try {
            int ping = client.getPing();

            System.out.println("Connected to Server!");
            System.out.println("Ping: " + ping + " ms");

            System.out.println();
            return true;
        } catch (UnreachableServerException e) {
            printErr("Err -1: Server Unreachable");
            printErr("FATAL ERROR: The session will be closed.");

            return false;
        }

    }

    private static void signup() throws CommandFailedException {
        System.out.println("----------- Signup -----------");
        System.out.print("Insert a username: ");
        String user = sc.nextLine();
        System.out.print("Insert a password: ");
        String pswd = sc.nextLine();

        client.signUp(user, pswd);

        System.out.println("Successfully signed up user: " + user);
    }

    private static void login() throws CommandFailedException {
        System.out.println("----------- Login -----------");
        System.out.print("Insert your username: ");
        user = sc.nextLine();
        System.out.print("Insert your password: ");
        String pswd = sc.nextLine();

        client.login(user, pswd);

        PROMPT = user + "@disbordissimo> ";
        System.out.println("Successfully logged in as " + user);
    }

    private static void printGreeting() {
        System.out.println("Welcome to Disbordissimo's CLI!");
        System.out.println("Type \"help\" to print a list of commands");
    }

    protected static void printErr(Object err) {
        System.out.println("\033[0;31m" + err + "\033[0m");
    }
    protected static void ping() {
        System.out.println("Current Ping: " + client.getPing() + " ms");
    }
    protected static void defaultErrHandling(int errCode) {
        switch (errCode) {
            case ReturnCodes.NO_PERMISSION -> printErr("Err 403: Forbidden");
            case ReturnCodes.COMMAND_NOT_FOUND -> printErr("Err 404: Command not found");
            case ReturnCodes.ERROR -> printErr("Err 500: Generic Server Error");
        }
    }
    protected static void unreachbleServerHandling() {
        printErr("Err -1: Server Unreachable");
        printErr("The connection will be closed!");
        System.exit(-1);
    }

    private static void sysErrRedirection() {
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {}
        }));
    }
}