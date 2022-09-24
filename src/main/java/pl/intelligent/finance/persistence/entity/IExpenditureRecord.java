package pl.intelligent.finance.persistence.entity;

public interface IExpenditureRecord {

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    Double getAmount();

    void setAmount(Double amount);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    String getBankStatementId();

    void setBankStatementId(String bankStatementId);

}
