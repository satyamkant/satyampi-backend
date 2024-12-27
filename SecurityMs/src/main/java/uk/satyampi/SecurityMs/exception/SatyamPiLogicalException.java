package uk.satyampi.SecurityMs.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SatyamPiLogicalException extends Exception {
    Exception exception;
    public SatyamPiLogicalException(String message, Exception e) {
        super(message);
        exception = e;
    }
}
