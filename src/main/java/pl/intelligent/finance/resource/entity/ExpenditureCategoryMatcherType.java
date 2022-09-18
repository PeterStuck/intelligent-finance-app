package pl.intelligent.finance.resource.entity;

public enum ExpenditureCategoryMatcherType {

    REGEX(1),
    NORMAL(2);

    private int id;

    ExpenditureCategoryMatcherType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ExpenditureCategoryMatcherType valueOf(Integer id) {
        if (id == null) {
            return null;
        }

        for (ExpenditureCategoryMatcherType type : values()) {
            if (type.id == id) {
                return type;
            }
        }

        return null;
    }

}
