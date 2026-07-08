package we.ytc.disbordissimo.cli;

import javax.sound.sampled.*;

public class OutManager {
    private final AudioFormat FORMAT = new AudioFormat(48000.0f, 16, 1, true, false);

    private SourceDataLine outLine;
    
    public OutManager() {
        try {
            DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, FORMAT);
            outLine = (SourceDataLine) AudioSystem.getLine(outInfo);

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void open() throws LineUnavailableException {
        outLine.open(FORMAT);
        outLine.start();
    }

    public void close() {
        outLine.close();
    }

    public void sound(byte[] byteArr) {
        outLine.write(byteArr, 0, byteArr.length);
    }
}
