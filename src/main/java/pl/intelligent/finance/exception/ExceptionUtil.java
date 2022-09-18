package pl.intelligent.finance.exception;

public class ExceptionUtil {

    public static InvalidDataException dataIntegrityError() {
        return new InvalidDataException("An exception occurred during transaction", InternalResultCode.DATABASE_INTEGRITY_ERROR);
    }

    public static InvalidDataException unexpectedError(String message) {
        return new InvalidDataException(message, InternalResultCode.ERROR);
    }

    public static InvalidDataException entityCreationError(String entityName) {
        return new InvalidDataException(entityName + "creation error", InternalResultCode.ENTITY_CREATION_ERROR);
    }

}
