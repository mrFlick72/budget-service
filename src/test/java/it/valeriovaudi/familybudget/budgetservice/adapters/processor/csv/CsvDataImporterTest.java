package it.valeriovaudi.familybudget.budgetservice.adapters.processor.csv;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId.emptyBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CsvDataImporterTest {

    @Test
    public void saveBudgetExpenseFromCsvFile() {
        List<BudgetExpense> expected =
                asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest\ntest", "super-market"));
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("budget-expense/importSample.csv")) {

            List<BudgetExpense> actual = new CsvDataImporter(";").loadData(resourceAsStream);
            assertThat(actual, is(expected));
        } catch (IOException e) {
            fail();
        }
    }
}