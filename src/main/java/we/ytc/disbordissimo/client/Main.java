package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.YtcLogger;

import java.net.InetAddress;
import java.util.Arrays;

public class Main {

    //Test
    public static void main(String[] args) throws Exception {
        long userID = 1234;
        long chID = 9876;

        DisbordissimoClient.Config config = new DisbordissimoClient.Config(InetAddress.getByName("localhost"), 6969);
        DisbordissimoClient client = DisbordissimoClient.create(config, new YtcLogger());

        client.setPacketReceivedHandler((audio) -> {
            System.out.println(Arrays.toString(audio));
        });
        client.setPacketSendingHandler(() -> {
            return new byte[1024];
        });

        Logger logger = new YtcLogger();

        //client.signUp("gigio", "123456");
        client.login("pippo", "123456");
//        client.join("voice1", "pippo's server");
//        boolean f = client.isConnectedTo("voice1", "pippo's server");
//        logger.logMsg(String.valueOf(f));
//
//        Thread.sleep(5000);
//        client.quit("voice1", "pippo's server");
//        Thread.sleep(1000);
//        client.quit("voice1", "pippo's server");
//        logger.logMsg(Arrays.toString(client.getGuilds()));
//        client.createGuild("pipo's server");
//        client.createGuildChannel("voice3", "pippo's server");
//        client.joinGuild("pipo's server");
//        logger.logMsg(Arrays.toString(client.getGuildChannels("pipo's server")));
//        logger.logMsg(client.getGuildOwner("pipo's server"));

        client.leaveGuild("pippo's server");
    }
}
