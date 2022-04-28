package pt.ubi.sd.g16.shared.Exceptions;

public class TopicIDTakenException extends Exception {
    public TopicIDTakenException() {
        super("This TopicID is already taken.");
    }
}
