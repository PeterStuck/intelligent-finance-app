package pl.intelligent.finance.resource.entity;

public interface StorableExpenditureRecord {

    Long getId();

    String getName();

    Double getAmount();

    Integer getCategoryId();

    Integer getBankStatementId();

}
