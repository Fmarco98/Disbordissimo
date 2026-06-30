package we.ytc.disbordissimo.client.exceptions;

public class IllegalMicFrameSize extends RuntimeException {

    public IllegalMicFrameSize(int size) {
        super("The mic frame has an invalid size (size="+size+")");
    }
}
