package it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget;


import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;

import java.util.List;

public record DailyBudgetExpense(List<BudgetExpense> budgetExpenseList,
                          Date date,
                          Money total
) {
}
