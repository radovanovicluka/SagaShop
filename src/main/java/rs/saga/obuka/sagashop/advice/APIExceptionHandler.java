package rs.saga.obuka.sagashop.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rs.saga.obuka.sagashop.exception.BudgetExceededException;
import rs.saga.obuka.sagashop.exception.ServiceException;

import java.time.LocalDateTime;

//@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class APIExceptionHandler {

    @ExceptionHandler(BudgetExceededException.class)
    public ResponseEntity<APIException> handleBudgetExceededException(BudgetExceededException exception) {
        APIException apiException = new APIException(exception.getMessage(), LocalDateTime.now());
        log.info("Nedovoljno sredstava", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

    @ExceptionHandler(ServiceException.class)
    private ResponseEntity<APIException> handleServiceExcpetion(ServiceException exception) {
        APIException apiException = new APIException(exception.getMessage(), LocalDateTime.now());
        log.info("Ocekivani izuzetak", exception);
        return ResponseEntity.status(exception.getErrorCode().getResponseCode()).body(apiException);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<APIException> handleSUnexpectedExcpetion(Exception exception) {
        APIException apiException = new APIException(exception.getMessage(), LocalDateTime.now());
        log.error("GRESKA!", exception);
        return ResponseEntity.status(500).body(apiException);
    }

//    U slucaju da se koristi validacija
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    ResponseEntity<ApiException> handleInvalidArgument(MethodArgumentNotValidException ex) {
//        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage) // Samo poruka greške
//                .toList();
//
//        String userFriendlyMessage = String.join(", ", errorMessages);
//
//        ApiException apiException = new ApiException(userFriendlyMessage, LocalDateTime.now());
//
//        log.info("Invalid arguments: {}", userFriendlyMessage, ex);
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
//    }

}
