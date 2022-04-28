package pt.ubi.sd.g16.shared.Exceptions;

public class PasswordNotMatchingException extends Exception {
        public PasswordNotMatchingException() {
            super("Password and Confirmation Password do not match.");
        }
    }