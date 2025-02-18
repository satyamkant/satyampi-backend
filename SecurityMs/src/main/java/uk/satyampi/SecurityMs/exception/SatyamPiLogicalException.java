package uk.satyampi.SecurityMs.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class SatyamPiLogicalException extends Exception {
    private final Throwable exception;
    private final String responseBody;
    private final HttpStatusCode statusCode;

    public SatyamPiLogicalException(String message, Throwable exception) {
        super(message);
        this.exception = exception;
        this.responseBody = null;
        this.statusCode = null;
    }

    public SatyamPiLogicalException(String message, Throwable exception, String responseBody, HttpStatusCode statusCode) {
        super(message);
        this.exception = exception;
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

}
