package it.valeriovaudi.familybudget.budgetservice.adapters.processor.excel;

import it.valeriovaudi.familybudget.budgetservice.adapters.processor.excel.builder.RowBuilder;
import it.valeriovaudi.familybudget.budgetservice.adapters.processor.excel.factory.CellStyleFactory;
import it.valeriovaudi.familybudget.budgetservice.adapters.processor.excel.factory.DailyBudgetExpenseRowFactory;
import it.valeriovaudi.familybudget.budgetservice.adapters.processor.excel.factory.FooterFactory;
import it.valeriovaudi.familybudget.budgetservice.adapters.processor.excel.factory.HeaderFactory;
import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.SearchTagRepository;
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

import static it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId.emptyBudgetExpenseId;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class XslxDataExporterTest {

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
        RowBuilder rowBuilder = new RowBuilder();
        XslxDataExporter pdfDataExporter = new XslxDataExporter(new FooterFactory(rowBuilder),
                new HeaderFactory(rowBuilder),
                new DailyBudgetExpenseRowFactory(rowBuilder, searchTagRepository),
                new CellStyleFactory());

        InputStream inputStream = pdfDataExporter.exportData(dataSource);


        Files.write(Paths.get(UUID.randomUUID().toString() + ".xlsx"), inputStream.readAllBytes());
    }
}