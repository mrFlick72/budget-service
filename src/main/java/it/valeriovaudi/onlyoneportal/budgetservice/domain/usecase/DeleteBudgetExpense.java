package it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpenseNotFoundException;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;

public class DeleteBudgetExpense {

    private final BudgetExpenseRepository budgetExpenseRepository;

    public DeleteBudgetExpense(BudgetExpenseRepository budgetExpenseRepository) {

        this.budgetExpenseRepository = budgetExpenseRepository;
    }

    public void delete(BudgetExpenseId budgetExpenseId) {
        budgetExpenseRepository.findFor(budgetExpenseId)
                .ifPresentOrElse(budgetExpense -> budgetExpenseRepository.delete(budgetExpense.getId()),
                        () -> {
                            throw new BudgetExpenseNotFoundException();
                        });
    }
}
