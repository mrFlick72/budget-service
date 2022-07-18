package it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetExpenseRepository;

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
