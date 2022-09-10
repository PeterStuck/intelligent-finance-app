package pl.intelligent.finance.exception;

public enum InternalResultCode {

    ERROR(500),
    DATABASE_INTEGRITY_ERROR(500),
    ENTITY_CREATION_ERROR(500);

    private int httpCode;

    InternalResultCode(int httpCode) {
        this.httpCode = httpCode;
    }

}
