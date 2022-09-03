package pl.intelligent.finance.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.intelligent.finance.entity.IExpenditureRecord;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "EXPENDITURE_RECORD")
public class ExpenditureRecord implements IExpenditureRecord {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "EXPENDITURE_RECORD_SEQ"
    )
    @SequenceGenerator(
            name = "EXPENDITURE_RECORD_SEQ",
            allocationSize = 5
    )
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "EXPENDITURE_CATEGORY_ID", nullable = false)
    private Integer categoryId;

    @Column(name = "BANK_STATEMENT_ID", nullable = false, length = 20)
    private String bankStatementId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public Integer getCategoryId() {
        return categoryId;
    }

    @Override
    public String getBankStatementId() {
        return bankStatementId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setBankStatementId(String bankStatementId) {
        this.bankStatementId = bankStatementId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenditureRecord that = (ExpenditureRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(amount, that.amount) && Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(bankStatementId, that.bankStatementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, categoryId, bankStatementId);
    }
}
