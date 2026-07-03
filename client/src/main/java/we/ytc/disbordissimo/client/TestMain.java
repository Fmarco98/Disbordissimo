package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.common.logger.Logger;
import we.ytc.disbordissimo.common.logger.YtcLogger;

import java.net.InetAddress;
import java.util.Arrays;

public class TestMain {

    //Test
    public static void main(String[] args) {
        DisbordissimoClient client = null;
        try {
            Logger logger = new YtcLogger();

            DisbordissimoClient.Config config = new DisbordissimoClient.Config(InetAddress.getByName("localhost"), 6969);
            client = DisbordissimoClient.create(config, logger);

            client.setPacketReceivedHandler((audio) -> {
                System.out.println(Arrays.toString(audio));
            });
            client.setPacketSendingHandler(() -> {
                return new byte[1024];
            });

//            logger.logMsg(client.getPing() +"");
            logger.logMsg(client.isServerReachable()+"");

//            client.signUp("gigio", "123456");
            client.login("pippo", "123456");
//            client.join("voice1", "pippo's server");
//            boolean f = client.isConnectedTo("voice1", "pippo's server");
//            logger.logMsg(String.valueOf(f));
//
//            Thread.sleep(5000);
//            client.quit("voice1", "pippo's server");
//            Thread.sleep(1000);
//            client.quit("voice1", "pippo's server");
//            logger.logMsg(Arrays.toString(client.getGuilds()));
//            client.createGuild("pipo's server");
//            client.createGuildChannel("voice3", "pippo's server");
//            client.joinGuild("pipo's server");
//            logger.logMsg(Arrays.toString(client.getGuildChannels("pipo's server")));
//            logger.logMsg(client.getGuildOwner("pipo's server"));
//            client.leaveGuild("pippo's server");

            logger.logMsg(Arrays.toString(client.getGuildMemers("pippo's server")));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.destroy();
        }
    }
}
