package uk.satyampi.SecurityMs.aop;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.satyampi.SecurityMs.dto.ResponseDto;
import uk.satyampi.SecurityMs.exception.SatyamPiLogicalException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdviceHandler {
    @ExceptionHandler(SatyamPiLogicalException.class)
    public ResponseEntity<?> exception(SatyamPiLogicalException e) {

        ResponseDto responseDto = new ResponseDto();

        switch (e.getMessage()){
            case "Http exception":
                responseDto.setError("Http exception");
                responseDto.setMessage(e.getMessage());
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            case "Server not available":
                responseDto.setError("Server not available");
                responseDto.setMessage(e.getMessage());
                return new ResponseEntity<>(responseDto, HttpStatus.SERVICE_UNAVAILABLE);
            case "Bad credentials":
                responseDto.setError("Authentication failed: wrong credentials");
                responseDto.setMessage(e.getMessage());
                return new ResponseEntity<>(responseDto,HttpStatus.UNAUTHORIZED);
            default:
                responseDto.setError(e.getMessage());
                responseDto.setMessage(e.getMessage());
                return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setError(e.getMessage());
        responseDto.setMessage(e.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
