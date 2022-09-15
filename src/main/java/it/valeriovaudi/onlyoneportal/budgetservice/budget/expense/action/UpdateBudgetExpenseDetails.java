package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;

public class UpdateBudgetExpenseDetails {
    private final BudgetExpenseRepository budgetExpenseRepository;

    public UpdateBudgetExpenseDetails(BudgetExpenseRepository budgetExpenseRepository) {

        this.budgetExpenseRepository = budgetExpenseRepository;
    }

    public void updateWithoutAttachment(BudgetExpense budgetExpense) {
        budgetExpenseRepository.findFor(budgetExpense.getId())
                .ifPresent(foundBudgetExpense -> {
                    BudgetExpense updatedBudgetExpense = new BudgetExpense(budgetExpense.getId(),
                            budgetExpense.getUserName(),
                            budgetExpense.getDate(),
                            budgetExpense.getAmount(), budgetExpense.getNote(),
                            budgetExpense.getTag()
                    );

                    budgetExpenseRepository.save(updatedBudgetExpense);
                });
    }
}
