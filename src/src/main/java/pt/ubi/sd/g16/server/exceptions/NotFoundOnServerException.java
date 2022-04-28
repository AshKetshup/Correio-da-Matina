package pt.ubi.sd.g16.server.exceptions;

public class NotFoundOnServerException extends Exception {
    private final String ip;

    public NotFoundOnServerException(String ip) {
        super("Not found on Server");

        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }
}
