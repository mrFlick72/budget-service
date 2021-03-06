package it.valeriovaudi.onlyoneportal.budgetservice.adapters.processor.pdf;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PdfDataExporterTest {

    @Mock
    SearchTagRepository searchTagRepository;

    @Test
    public void name() throws IOException {
        given(searchTagRepository.findSearchTagBy("super-market"))
                .willReturn(new SearchTag("super-market", "Spesa"));

        List<BudgetExpense> dataSource =
                asList(new BudgetExpense(BudgetExpenseId.emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest", "super-market"),
                        new BudgetExpense(BudgetExpenseId.emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(BudgetExpenseId.emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("05/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(BudgetExpenseId.emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("12/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest", "super-market"),
                        new BudgetExpense(BudgetExpenseId.emptyBudgetExpenseId(), new UserName("USER"), Date.dateFor("01/02/2018"), Money.moneyFor("12.50"), "...\ntest\ntest\ntest", "super-market"));
        PdfDataExporter pdfDataExporter = new PdfDataExporter(searchTagRepository);
        InputStream inputStream = pdfDataExporter.exportData(dataSource);


        Files.write(Paths.get(UUID.randomUUID().toString() + ".pdf"), inputStream.readAllBytes());
    }
}