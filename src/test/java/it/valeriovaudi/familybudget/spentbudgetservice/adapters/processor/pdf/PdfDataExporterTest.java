package it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.pdf;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.SearchTagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId.emptyBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PdfDataExporterTest {

    @Mock
    SearchTagRepository searchTagRepository;

    @Test
    public void name() throws IOException {
        given(searchTagRepository.findSearchTagBy("super-market"))
                .willReturn(new SearchTag("super-market", "Spesa"));

        List<BudgetExpense> dataSource =
                asList(new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest\ntest", "super-market"));
        PdfDataExporter pdfDataExporter = new PdfDataExporter(searchTagRepository);
        InputStream inputStream = pdfDataExporter.exportData(dataSource);


        Files.write(Paths.get(UUID.randomUUID().toString() + ".pdf"), inputStream.readAllBytes());
    }
}