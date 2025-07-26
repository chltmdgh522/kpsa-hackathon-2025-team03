package game3.global.infra.exception.error;


public class MindException extends RuntimeException {

    private final ErrorCode errorCode;

    public MindException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public MindException(ErrorCode errorCode, String detailMessage) {
        super(errorCode.getMessage() + " → " + detailMessage);
        this.errorCode = errorCode;
    }


    public MindException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public int getHttpStatusCode() {
        return errorCode.getHttpCode();
    }
}
