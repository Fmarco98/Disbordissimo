package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.client.commands.JoinCommand;
import we.ytc.disbordissimo.client.commands.QuitCommand;
import we.ytc.disbordissimo.TempConfig;
import we.ytc.disbordissimo.common.logger.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Main {

    private static Logger logger = null;

    public static class Config {
        public static String TCP_HOST = TempConfig.TCP_HOST;
        public static int TCP_PORT = TempConfig.TCP_PORT;
    }

    //Test
    public static void main(String[] args) throws Exception {
        long userID = 1234;
        long chID = 9876;

        JoinCommand join = new JoinCommand();
        join.execute(String.valueOf(userID), String.valueOf(chID));

        DatagramSocket s = new DatagramSocket();
        byte[] data = "ciao".getBytes();
        ByteBuffer bbuf = ByteBuffer.allocate(8+1024);
        bbuf.putLong(userID);
        bbuf.put(data);

        bbuf.flip();

        byte[] out = bbuf.array();
        DatagramPacket p = new DatagramPacket(out, out.length, InetAddress.getByName(TempConfig.UDP_HOST), TempConfig.UDP_PORT);
        s.send(p);
        p = new DatagramPacket(out, out.length);
        s.receive(p);
        Main.getLogger().logMsg(new String(p.getData()));

        Thread.sleep(5000);

//        QuitCommand quit = new QuitCommand();
//        quit.execute(String.valueOf(userID), String.valueOf(chID));
    }

    public static Logger getLogger() {
        if(logger == null) {
            logger = new Logger(); //TODO: log setup conf
        }
        return logger;
    }
}
