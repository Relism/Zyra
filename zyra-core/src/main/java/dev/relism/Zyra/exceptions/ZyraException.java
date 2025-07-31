package dev.relism.Zyra.exceptions;

public abstract class ZyraException extends RuntimeException {

    public ZyraException(String message) {
        super(formatMessage(message));
    }

    public ZyraException(String message, Throwable cause) {
        super(formatMessage(message), cause);
    }

    private static String formatMessage(String raw) {
        return raw;
    }
}

