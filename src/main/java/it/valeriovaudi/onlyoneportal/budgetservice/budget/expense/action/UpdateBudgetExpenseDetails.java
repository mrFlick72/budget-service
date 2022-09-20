package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;

public class UpdateBudgetExpenseDetails {
    private final BudgetExpenseRepository budgetExpenseRepository;

    public UpdateBudgetExpenseDetails(BudgetExpenseRepository budgetExpenseRepository) {

        this.budgetExpenseRepository = budgetExpenseRepository;
    }

    public void updateWithoutAttachment(BudgetExpense budgetExpense) {
        budgetExpenseRepository.findFor(budgetExpense.id())
                .ifPresent(foundBudgetExpense -> {
                    BudgetExpense updatedBudgetExpense = new BudgetExpense(budgetExpense.id(),
                            budgetExpense.userName(),
                            budgetExpense.date(),
                            budgetExpense.amount(), budgetExpense.note(),
                            budgetExpense.tag()
                    );

                    budgetExpenseRepository.save(updatedBudgetExpense);
                });
    }
}
