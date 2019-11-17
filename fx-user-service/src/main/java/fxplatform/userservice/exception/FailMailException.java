package fxplatform.userservice.exception;

public class FailMailException extends RuntimeException {
    public FailMailException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
