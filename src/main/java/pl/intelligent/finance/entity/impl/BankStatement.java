package pl.intelligent.finance.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.intelligent.finance.entity.IBankStatement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "BANK_STATEMENT")
public class BankStatement implements IBankStatement {

    @Id
    @Column(name = "ID", nullable = false, length = 20)
    private String id;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
