package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

public abstract class AbstractSearchTagRepositoryIT {
    SearchTagRepository budgetExpenseRepository;
    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void findAll() {
        Assertions.assertEquals(budgetExpenseRepository.findAllSearchTag().size(), 24);
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void findSearchTagBy() {
        Assertions.assertEquals(budgetExpenseRepository.findSearchTagBy("super-market"), new SearchTag("super-market", "Spesa"));
    }

    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void delete() {
        budgetExpenseRepository.delete("loan");
        SearchTag actual = budgetExpenseRepository.findSearchTagBy("loan");
        Assertions.assertNull(actual);
    }


    @Test
    @Sql("classpath:/search_tag/findAll.sql")
    public void save() {
        budgetExpenseRepository.save(new SearchTag("test", "Test"));
        SearchTag actual = budgetExpenseRepository.findSearchTagBy("test");
        Assertions.assertEquals(new SearchTag("test", "Test"), actual);
    }
}