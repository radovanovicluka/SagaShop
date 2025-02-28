package rs.saga.obuka.sagashop.exception;

/**
 * @author: Ana Dedović
 * Date: 28.06.2021.
 **/
@SuppressWarnings("unused")
public class ServiceException extends Exception {

    private ErrorCode errorCode;
    private Object[] params;

    public ServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode, Object... params) {
        this.errorCode = errorCode;
        this.params = params;
    }

    public ServiceException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    public ServiceException(ErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode, String errorMessage, Exception e) {
        super(errorMessage, e);
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode, String errorMessage, Object... params) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.params = params;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }


}
