package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static it.valeriovaudi.onlyoneportal.budgetservice.BudgetFixture.saltGenerator;

class BudgetDynamoDbIdFactoryTest {

    public static final BudgetExpense BUDGET_EXPENSE = new BudgetExpense(
            new BudgetExpenseId("AN_ID"),
            new UserName("USER"),
            Date.dateFor("01/01/2018"),
            Money.ONE,
            "",
            ""
    );

    @Test
    void makeAPartitionKey() {
        BudgetDynamoDbIdFactory budgetDynamoDbIdFactory = new BudgetDynamoDbIdFactory(saltGenerator);
        String actual = budgetDynamoDbIdFactory.partitionKeyFor(BUDGET_EXPENSE);


        Assertions.assertEquals("MjAxOF8xX1VTRVI=", actual);
    }

    @Test
    void makeARangeKey() {
        BudgetDynamoDbIdFactory budgetDynamoDbIdFactory = new BudgetDynamoDbIdFactory(saltGenerator);
        String actual = budgetDynamoDbIdFactory.rangeKeyFor(BUDGET_EXPENSE);

        Assertions.assertEquals("MV9BTl9JRA==", actual);
    }
}