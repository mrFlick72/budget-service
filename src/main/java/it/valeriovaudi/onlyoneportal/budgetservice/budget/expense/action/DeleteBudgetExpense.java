package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseNotFoundException;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.repository.BudgetExpenseRepository;

public class DeleteBudgetExpense {

    private final BudgetExpenseRepository budgetExpenseRepository;

    public DeleteBudgetExpense(BudgetExpenseRepository budgetExpenseRepository) {

        this.budgetExpenseRepository = budgetExpenseRepository;
    }

    public void delete(BudgetExpenseId budgetExpenseId) {
        budgetExpenseRepository.findFor(budgetExpenseId)
                .ifPresentOrElse(budgetExpense -> budgetExpenseRepository.delete(budgetExpense.id()),
                        () -> {
                            throw new BudgetExpenseNotFoundException();
                        });
    }
}
