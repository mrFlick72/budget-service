package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import org.junit.jupiter.api.Assertions;


public abstract class AbstractSearchTagRepositoryIT {


    public void findAll(SearchTagRepository jdbcBudgetExpenseRepository) {
        Assertions.assertEquals(jdbcBudgetExpenseRepository.findAllSearchTag().size(), 24);
    }

    public void findSearchTagBy(SearchTagRepository jdbcBudgetExpenseRepository) {
        Assertions.assertEquals(jdbcBudgetExpenseRepository.findSearchTagBy("super-market"), new SearchTag("super-market", "Spesa"));
    }

    public void delete(SearchTagRepository jdbcBudgetExpenseRepository) {
        jdbcBudgetExpenseRepository.delete("loan");
        SearchTag actual = jdbcBudgetExpenseRepository.findSearchTagBy("loan");
        Assertions.assertNull(actual);
    }


    public void save(SearchTagRepository jdbcBudgetExpenseRepository) {
        jdbcBudgetExpenseRepository.save(new SearchTag("test", "Test"));
        SearchTag actual = jdbcBudgetExpenseRepository.findSearchTagBy("test");
        Assertions.assertEquals(new SearchTag("test", "Test"), actual);
    }
}