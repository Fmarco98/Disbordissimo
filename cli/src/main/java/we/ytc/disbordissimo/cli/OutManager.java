package we.ytc.disbordissimo.cli;

import javax.sound.sampled.*;

/**
 * <h1>OutManager</h1>
 *
 * Class that handles all of the client's audio output communications
 */
public class OutManager {
    private final AudioFormat FORMAT = new AudioFormat(48000.0f, 16, 1, true, false);

    private SourceDataLine outLine;

    /**
     * This constructor takes the default Audio Output Device of the system and prepares a compatible audio stream
     */
    public OutManager() {
        try {
            DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, FORMAT);
            outLine = (SourceDataLine) AudioSystem.getLine(outInfo);

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens the Audio Output Device
     * @throws LineUnavailableException
     */
    public void open() throws LineUnavailableException {
        outLine.open(FORMAT);
        outLine.start();
    }

    /**
     * Closes the Audio Output Device
     */
    public void close() {
        outLine.close();
    }

    /**
     * Writes to the Audio Output Device a {@code byte[]}
     * @param byteArr
     */
    public void write(byte[] byteArr) {
        outLine.write(byteArr, 0, byteArr.length);
    }
}
