package pl.intelligent.finance.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import pl.intelligent.finance.persistence.entity.IBankStatement;
import pl.intelligent.finance.service.IBankStatementService;
import pl.intelligent.finance.service.IExpenditureRecordService;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BankStatementServiceDbTest extends ServiceTestBase {

    @Autowired
    @Qualifier("bankStatementServiceDb")
    private IBankStatementService bsService;

    @Autowired
    @Qualifier("expenditureRecordServiceDb")
    private IExpenditureRecordService recordService;

    @Test
    @DisplayName("Should create new Bank Statement without any associations")
    public void createTest() throws Exception {
        var bs = bsService.createInstance();
        bs.setName("testId");

        IBankStatement persistedBs = bsService.create(bs);
        assertEquals(bs, persistedBs);

        IBankStatement storedBs = bsService.findById(persistedBs.getId());
        assertNotNull(storedBs);
        assertEquals(persistedBs, storedBs);
    }

    @Test
    @DisplayName("Should throw integrity violation when bsId already exists")
    public void createBsIdAlreadyExistsTest() throws Exception {
        IBankStatement storedBs = bsService.findById(STORED_BANK_STATEMENT_ID);
        assertNotNull(storedBs);

        var bs = bsService.createInstance();
        bs.setName(STORED_BANK_STATEMENT);

        assertThrows(DataIntegrityViolationException.class, () -> bsService.create(bs));
    }

    @Test
    @DisplayName("Should delete Bank Statement when there's no associations")
    public void deleteByIdTest() throws Exception {
        IBankStatement storedBs = bsService.findById(STORED_BANK_STATEMENT_ID2);
        assertNotNull(storedBs);

        var records = recordService.findByBankStatementId(storedBs.getId());
        assertEquals(0, records.size());

        bsService.deleteById(STORED_BANK_STATEMENT_ID2, false);

        storedBs = bsService.findById(STORED_BANK_STATEMENT_ID2);
        assertNull(storedBs);
    }

    @Test
    @DisplayName("Should delete Bank Statement when there's associations and force remove flag")
    public void deleteByIdWithRecordsForceRemoveFlagTest() throws Exception {
        IBankStatement storedBs = bsService.findById(STORED_BANK_STATEMENT_ID);
        assertNotNull(storedBs);

        var records = recordService.findByBankStatementId(storedBs.getId());
        assertEquals(4, records.size());

        bsService.deleteById(STORED_BANK_STATEMENT_ID2, true);

        storedBs = bsService.findById(STORED_BANK_STATEMENT_ID2);
        assertNull(storedBs);
    }

    @Test
    @DisplayName("Should throw exception when try to delete Bank Statement with associations")
    public void deleteByIdWithRecordsTest() throws Exception {
        IBankStatement storedBs = bsService.findById(STORED_BANK_STATEMENT_ID);
        assertNotNull(storedBs);

        var records = recordService.findByBankStatementId(storedBs.getId());
        assertEquals(4, records.size());

//        bsService.deleteById(STORED_BANK_STATEMENT_ID2, false);

        assertThrows(DataIntegrityViolationException.class, () -> bsService.deleteById(STORED_BANK_STATEMENT_ID2, false));
    }

}
