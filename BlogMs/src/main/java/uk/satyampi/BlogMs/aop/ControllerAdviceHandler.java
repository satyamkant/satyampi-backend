package uk.satyampi.BlogMs.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.satyampi.BlogMs.dto.ResponseDTO;
import uk.satyampi.BlogMs.exception.SatyamPiLogicalException;

@RestControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(SatyamPiLogicalException.class)
    public ResponseEntity<?> exception(SatyamPiLogicalException e){
        switch (e.getMessage()) {
            case "Blog Not Found" -> {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
            case "DataIntegrityViolationException occurred"->{
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
            default -> {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    public ResponseEntity<?> exception(UnexpectedRollbackException e){
        return new ResponseEntity<>("Blog Title Already Exist", HttpStatus.CONFLICT);
    }
}
