package it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date;

public record BudgetRevenue(    //todo make it a BudgetRevenueId
                         String id,

                         //todo make it a UserName
                         String userName,
                         Date registrationDate,
                         Money amount,
                         String note

) {

}
