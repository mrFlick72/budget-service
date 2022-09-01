package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class BudgetDynamoDbIdFactoryTest {

    public static final BudgetExpense BUDGET_EXPENSE = new BudgetExpense(
            new BudgetExpenseId(UUID.randomUUID().toString()),
            new UserName("USER"),
            Date.dateFor("01/01/2018"),
            Money.ONE,
            "",
            ""
    );

    @Test
    void makeAPartitionKey() {
        BudgetDynamoDbIdFactory budgetDynamoDbIdFactory = new BudgetDynamoDbIdFactory();
        String actual = budgetDynamoDbIdFactory.partitionKeyFor(BUDGET_EXPENSE);


        Assertions.assertEquals("2018_1_USER", actual);
    }

    @Test
    void makeARangeKey() {
        BudgetDynamoDbIdFactory budgetDynamoDbIdFactory = new BudgetDynamoDbIdFactory();
        String actual = budgetDynamoDbIdFactory.rangeKeyFor(BUDGET_EXPENSE);

        Assertions.assertEquals("1_"+ BUDGET_EXPENSE.getId().getContent(), actual);
    }
}