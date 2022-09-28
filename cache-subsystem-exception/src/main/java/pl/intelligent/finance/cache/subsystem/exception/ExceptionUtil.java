package pl.intelligent.finance.cache.subsystem.exception;

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

    public static InvalidDataException entityUpdateError(String entityName) {
        return new InvalidDataException(entityName + "update error", InternalResultCode.ENTITY_CREATION_ERROR);
    }

    public static InvalidDataException expenditureCategoryMatcherNotFound(String categoryName, int matcherId) {
        return new InvalidDataException(String.format("Matcher with id: '%d' not found in category: '%s'", matcherId, categoryName),
                InternalResultCode.EXPENDITURE_CATEGORY_MATCHER_NOT_FOUND);
    }

    public static InvalidDataException expenditureCategoryMatcherNotFound(String categoryName, String pattern) {
        return new InvalidDataException(String.format("Matcher with pattern: '%s' not found in category: '%s'", pattern, categoryName),
                InternalResultCode.EXPENDITURE_CATEGORY_MATCHER_NOT_FOUND);
    }

}
