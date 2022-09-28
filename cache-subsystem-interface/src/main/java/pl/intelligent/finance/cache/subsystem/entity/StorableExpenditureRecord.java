package pl.intelligent.finance.cache.subsystem.entity;

public interface StorableExpenditureRecord {

    Long getId();

    String getName();

    Double getAmount();

    Integer getCategoryId();

    Integer getBankStatementId();

}
