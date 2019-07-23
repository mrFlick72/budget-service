package it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.processor.DataExporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExportSpentBudgetTest {


    private static final Month JANUARY = Month.JANUARY;
    private static final Year NOW = Year.of(2018);

    @Mock
    private FindSpentBudget findSpentBudget;

    @Mock
    private DataExporter dataExporter;

    @Test
    public void exportSpentBudget() {
        ExportSpentBudget exportSpentBudget = new ExportSpentBudget(findSpentBudget, dataExporter);

        BudgetExpense budgetExpense = new BudgetExpense(new BudgetExpenseId(""),
                new UserName(""),
                Date.dateFor("10/10/2018"),
                Money.ONE, "", "");

        SpentBudget spentBudget = new SpentBudget(Collections.singletonList(budgetExpense), Collections.emptyList());

        given(findSpentBudget.findBy(JANUARY, NOW, Collections.emptyList()))
                .willReturn(spentBudget);

        exportSpentBudget.exportFrom(NOW, JANUARY);

        verify(findSpentBudget).findBy(JANUARY, NOW, Collections.emptyList());
        verify(dataExporter).exportData(Collections.singletonList(budgetExpense));
    }
}