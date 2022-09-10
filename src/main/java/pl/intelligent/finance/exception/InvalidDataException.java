package pl.intelligent.finance.exception;

public class InvalidDataException extends Exception {

    private InternalResultCode resultCode;

    public InvalidDataException(String message, InternalResultCode resultCode) {
        super(message);
        this.resultCode = resultCode;
    }

    public InvalidDataException(String message) {
        super(message);
        this.resultCode = InternalResultCode.ERROR;
    }

    public InternalResultCode getResultCode() {
        return resultCode;
    }
}
