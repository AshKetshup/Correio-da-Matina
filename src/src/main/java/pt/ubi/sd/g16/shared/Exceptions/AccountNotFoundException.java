package pt.ubi.sd.g16.shared.Exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException() {
        super("Given account doesn't exist");
    }

}
