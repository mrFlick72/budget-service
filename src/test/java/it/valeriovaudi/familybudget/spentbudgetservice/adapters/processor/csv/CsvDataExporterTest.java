package it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.csv;


import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId.emptyBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CsvDataExporterTest {

    @Test
    public void writeBudgetExpenseOnCsvFile() {
        List<BudgetExpense> dataSource =
                asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest\ntest", "super-market"));
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("budget-expense/importSample.csv")) {
            byte[] actual = new CsvDataExporter(";").exportData(dataSource).readAllBytes();
            byte[] expected = resourceAsStream.readAllBytes();
            assertThat(actual, is(expected));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void budgetExpenseListToCsvContentAsString() {
        List<BudgetExpense> dataSource =
                asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest\ntest", "super-market"));
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("budget-expense/importSample.csv")) {
            List<String> actual = new CsvDataExporter(";").formatFileRaw(dataSource);
            List<String> expected = Arrays.asList(new String(resourceAsStream.readAllBytes()).split("\n"));

            assertThat(actual, is(expected));
        } catch (IOException e) {
            fail();
        }
    }
}