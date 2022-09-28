package pl.intelligent.finance.persistence.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.intelligent.finance.persistence.entity.IBankStatement;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BANK_STATEMENT")
public class BankStatement implements IBankStatement {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BANK_STATEMENT_SEQ"
    )
    @SequenceGenerator(
            name = "BANK_STATEMENT_SEQ",
            allocationSize = 5
    )
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankStatement that = (BankStatement) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "BankStatement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
