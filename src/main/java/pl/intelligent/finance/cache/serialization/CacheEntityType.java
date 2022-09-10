package pl.intelligent.finance.cache.serialization;

public enum CacheEntityType {

    EXPENDITURE_RECORD_TYPE(1);

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
