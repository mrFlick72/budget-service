package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb.BudgetDynamoDbIdFactory;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static it.valeriovaudi.onlyoneportal.budgetservice.support.BudgetFixture.saltGenerator;

class BudgetDynamoDbIdFactoryTest {

    public static final BudgetExpense BUDGET_EXPENSE = new BudgetExpense(
            null,
            new UserName("USER"),
            Date.dateFor("01/01/2018"),
            Money.ONE,
            "",
            ""
    );


    @Test
    void getACompleteBudgetId() {
        BudgetDynamoDbIdFactory budgetDynamoDbIdFactory = new BudgetDynamoDbIdFactory(saltGenerator);
        BudgetExpenseId actual = budgetDynamoDbIdFactory.budgetIdFrom(BUDGET_EXPENSE);

        Assertions.assertEquals("MjAxOF8xX1VTRVI=-MV9BX1NBTFQ=", actual.getContent());
    }
}