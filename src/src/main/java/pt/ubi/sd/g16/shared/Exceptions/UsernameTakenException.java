package pt.ubi.sd.g16.shared.Exceptions;

public class UsernameTakenException extends Exception {
    public UsernameTakenException() {
        super("This Username is already taken.");
    }
}