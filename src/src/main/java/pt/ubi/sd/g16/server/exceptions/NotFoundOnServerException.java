package pt.ubi.sd.g16.server.exceptions;

public class NotFoundOnServerException extends Exception {
    private final String ip;

    public NotFoundOnServerException(String ip) {
        super("Not found on Server");

        this.ip = ip;
    }

    public NotFoundOnServerException() {
        super("Not found on Server");

        this.ip = null;
    }

    public String getIp() {
        return ip;
    }
}
