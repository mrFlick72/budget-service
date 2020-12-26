package it.valeriovaudi.familybudget.budgetservice.domain.usecase;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.budgetservice.domain.processor.DataImporter;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.BudgetExpenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId.emptyBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LoadSpentBudgetExpenseTest {

    @Mock
    BudgetExpenseRepository budgetExpenseRepository;

    @Mock
    DataImporter dataImporter;

    @Test
    public void saveBudgetExpenseFromCsvFile() {
        List<BudgetExpense> budgetExpenses = asList(
                new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "super market", "super market"),
                new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "super market", "super market"),
                new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "super market", "super market"));

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("budget-expense/importSample.csv")) {

            given(dataImporter.loadData(inputStream))
                    .willReturn(budgetExpenses);

            new LoadSpentBudget(budgetExpenseRepository, dataImporter).importFrom(inputStream);

            verify(dataImporter).loadData(inputStream);

            verify(budgetExpenseRepository, times(3)).save(Mockito.any(BudgetExpense.class));
        } catch (IOException e) {
            Assertions.fail();
        }
    }
}
