package pt.ubi.sd.g16.shared.Exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("Wrong Password");
    }
}
