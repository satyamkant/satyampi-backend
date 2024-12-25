package uk.satyampi.UserMs.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.satyampi.UserMs.exception.SatyamPiLogicalException;

@RestControllerAdvice
public class ControllerAdviceHandler {

    private final Logger logger = LogManager.getLogger(this.getClass());
    @ExceptionHandler(SatyamPiLogicalException.class)
    public ResponseEntity<String> exception(SatyamPiLogicalException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
