package uk.satyampi.BlogMs.aop;

import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.satyampi.BlogMs.dto.ResponseDTO;
import uk.satyampi.BlogMs.exception.SatyamPiLogicalException;

@RestControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(SatyamPiLogicalException.class)
    public ResponseEntity<?> exception(SatyamPiLogicalException e){
        switch (e.getMessage()){
            case "Blog Not Found":
                return new ResponseEntity<>(new ResponseDTO(null,HttpStatus.NOT_FOUND.toString(),e.getMessage(),null), HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(new ResponseDTO(null,HttpStatus.BAD_REQUEST.toString(),e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

}
