package it.valeriovaudi.familybudget.spentbudgetservice.web.config;

import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.csv.CsvDataExporter;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.csv.CsvDataImporter;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.excel.XslxDataExporter;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.excel.builder.RowBuilder;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.excel.factory.CellStyleFactory;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.excel.factory.DailyBudgetExpenseRowFactory;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.excel.factory.FooterFactory;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.excel.factory.HeaderFactory;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.processor.pdf.PdfDataExporter;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.IdProvider;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.*;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessLogicConfiguration {

    private static final String SEPARATOR = ";";

    @Bean
    public DeleteBudgetExpense deleteBudgetExpense(BudgetExpenseRepository budgetExpenseRepository,
                                                   AttachmentRepository attachmentRepository) {
        return new DeleteBudgetExpense(budgetExpenseRepository, attachmentRepository);
    }

    @Bean
    public UpdateBudgetExpenseDetails updateBudgetExpenseDetails(BudgetExpenseRepository budgetExpenseRepository) {
        return new UpdateBudgetExpenseDetails(budgetExpenseRepository);
    }

    @Bean
    public DeleteBudgetExpenseAttachment deleteBudgetExpenseAttachment(AttachmentRepository attachmentRepository,
                                                                       BudgetExpenseRepository budgetExpenseRepository) {
        return new DeleteBudgetExpenseAttachment(budgetExpenseRepository, attachmentRepository);
    }

    @Bean
    public GetBudgetExpenseAttachment getBudgetExpenseAttachment(AttachmentRepository attachmentRepository,
                                                                 BudgetExpenseRepository budgetExpenseRepository) {
        return new GetBudgetExpenseAttachment(attachmentRepository, budgetExpenseRepository);
    }

    @Bean
    public FindBudgetRevenue findBudgetRevenue(UserRepository userRepository,
                                               BudgetRevenueRepository budgetRevenueRepository) {
        return new FindBudgetRevenue(budgetRevenueRepository, userRepository);
    }

    @Bean
    public FindSpentBudget findSpentBudget(UserRepository userRepository,
                                           SearchTagRepository searchTagRepository,
                                           BudgetExpenseRepository budgetExpenseRepository) {
        return new FindSpentBudget(userRepository, budgetExpenseRepository, searchTagRepository);
    }

    @Bean
    public LoadSpentBudget loadSpentBudget(BudgetExpenseRepository budgetExpenseRepository) {
        return new LoadSpentBudget(budgetExpenseRepository, new CsvDataImporter(SEPARATOR));
    }

    @Bean
    public ExportSpentBudget csvSpentBudgetExporter(FindSpentBudget findSpentBudget) {
        return new ExportSpentBudget(findSpentBudget, new CsvDataExporter(SEPARATOR));
    }

    @Bean
    public ExportSpentBudget pdfSpentBudgetExporter(FindSpentBudget findSpentBudget,
                                                    SearchTagRepository searchTagRepository) {
        return new ExportSpentBudget(findSpentBudget, new PdfDataExporter(searchTagRepository));
    }

    @Bean
    public CreateBudgetExpense createBudgetExpense(UserRepository userRepository,
                                                   BudgetExpenseRepository budgetExpenseRepository,
                                                   AttachmentRepository attachmentRepository,
                                                   IdProvider idProvider) {
        return new CreateBudgetExpense(budgetExpenseRepository, attachmentRepository, userRepository, idProvider);
    }

    @Bean
    public ExportSpentBudget xslxDataExporter(FindSpentBudget findSpentBudget,
                                              SearchTagRepository searchTagRepository) {
        RowBuilder rowBuilder = new RowBuilder();
        return new ExportSpentBudget(findSpentBudget, new XslxDataExporter(new FooterFactory(rowBuilder),
                new HeaderFactory(rowBuilder),
                new DailyBudgetExpenseRowFactory(rowBuilder, searchTagRepository),
                new CellStyleFactory()));
    }
}
