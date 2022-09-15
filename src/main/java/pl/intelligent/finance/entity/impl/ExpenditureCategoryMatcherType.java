package pl.intelligent.finance.entity.impl;

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
}
