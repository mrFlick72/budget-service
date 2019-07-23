package it.valeriovaudi.familybudget.spentbudgetservice.web.adapter;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.DailyBudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.familybudget.spentbudgetservice.web.model.BudgetExpenseRepresentation;
import it.valeriovaudi.familybudget.spentbudgetservice.web.model.DailyBudgetExpenseRepresentation;
import it.valeriovaudi.familybudget.spentbudgetservice.web.model.SpentBudgetRepresentation;
import it.valeriovaudi.familybudget.spentbudgetservice.web.model.TotalBySearchTagDetail;

import java.util.List;

import static java.util.stream.Collectors.toList;


public class SpentBudgetAdapter {
    private final BudgetExpenseAdapter budgetExpenseAdapter;

    public SpentBudgetAdapter(BudgetExpenseAdapter budgetExpenseAdapter) {
        this.budgetExpenseAdapter = budgetExpenseAdapter;
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
                .map(budgetExpenseAdapter::domainToRepresentationModel)
                .collect(toList());
    }


}
