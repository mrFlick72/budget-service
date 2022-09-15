package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint.BudgetExpenseRepresentation;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint.DailyBudgetExpenseRepresentation;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint.SpentBudgetRepresentation;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint.TotalBySearchTagDetail;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.DailyBudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.SpentBudget;

import java.util.List;

import static java.util.stream.Collectors.toList;


public class SpentBudgetConverter {
    private final BudgetExpenseConverter budgetExpenseConverter;

    public SpentBudgetConverter(BudgetExpenseConverter budgetExpenseConverter) {
        this.budgetExpenseConverter = budgetExpenseConverter;
    }

    public SpentBudgetRepresentation domainToRepresentationModel(SpentBudget spentBudget) {
        return new SpentBudgetRepresentation(spentBudget.total().stringifyAmount(),
                spentBudget.dailyBudgetExpenseList().stream()
                        .map(dailyBudgetExpense ->
                                new DailyBudgetExpenseRepresentation(budgetExpenseRepresentationList(dailyBudgetExpense),
                                        dailyBudgetExpense.getDate().formattedDate(),
                                        dailyBudgetExpense.getTotal().stringifyAmount())).collect(toList()),
                spentBudget.totalForSearchTags().entrySet().stream()
                        .map(total -> new TotalBySearchTagDetail(total.getKey().getKey(),
                                total.getKey().getValue(),
                                total.getValue().stringifyAmount()))
                        .collect(toList()));
    }

    private List<BudgetExpenseRepresentation> budgetExpenseRepresentationList(DailyBudgetExpense dailyBudgetExpense) {
        return dailyBudgetExpense.getBudgetExpenseList().stream()
                .map(budgetExpenseConverter::domainToRepresentationModel)
                .collect(toList());
    }


}
