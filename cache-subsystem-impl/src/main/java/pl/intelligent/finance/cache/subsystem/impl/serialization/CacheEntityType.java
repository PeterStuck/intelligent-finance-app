package pl.intelligent.finance.cache.subsystem.impl.serialization;

public enum CacheEntityType {

    EXPENDITURE_RECORD_TYPE(1),
    EXPENDITURE_CATEGORY_TYPE(2),
    EXPENDITURE_CATEGORY_MATCHER_TYPE(3);

    private int typeId;

    CacheEntityType(int typeId) {
        this.typeId = typeId;
    }

    public static CacheEntityType getTypeById(int typeId) {
        for (CacheEntityType type : values()) {
            if (type.getTypeId() == typeId) {
                return type;
            }
        }

        return null;
    }

    public int getTypeId() {
        return typeId;
    }

}
