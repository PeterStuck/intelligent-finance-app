package pl.intelligent.finance.service;

import pl.intelligent.finance.persistence.entity.IBankStatement;

import java.util.Collection;
import java.util.List;

public interface IBankStatementService {

    IBankStatement findById(Integer id);

    List<IBankStatement> findByIds(Collection<Integer> ids);

    List<Integer> findAllIds();

    IBankStatement createInstance();

    IBankStatement create(IBankStatement bankStatement) throws Exception;

    void deleteById(Integer bsId, boolean forceRemove);

    void delete(IBankStatement bankStatement, boolean forceRemove);

}
