package pl.intelligent.finance.persistence.repository.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;
import pl.intelligent.finance.persistence.repository.BankStatementRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BankStatementRepositoryTest extends RepositoryTestBase {

    @Autowired
    private BankStatementRepository repository;

    @Test
    @DisplayName("Should return all stored bank statement ids")
    public void findAllIdsTest() {
        List<Integer> allIds = repository.findAllIds();
        assertEquals(2, allIds.size());
        assertTrue(allIds.contains(STORED_BANK_STATEMENT_ID));
        assertTrue(allIds.contains(STORED_BANK_STATEMENT_ID2));
    }

    @Test
    @DisplayName("Should return empty list when none bank statements are stored")
    @Sql(scripts={"/sql/02_create_tables.sql"})
    public void findAllIdsEmptyListTest() {
        List<Integer> allIds = repository.findAllIds();
        assertEquals(0, allIds.size());
    }

}
