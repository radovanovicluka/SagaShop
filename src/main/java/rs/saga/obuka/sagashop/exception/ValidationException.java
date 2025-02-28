package rs.saga.obuka.sagashop.exception;

public class ValidationException extends RuntimeException {

    private ErrorCode errorCode;

    public ValidationException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

}
