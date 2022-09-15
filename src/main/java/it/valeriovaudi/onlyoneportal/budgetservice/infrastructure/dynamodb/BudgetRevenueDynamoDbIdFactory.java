package it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueId;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;

import java.util.Base64;
import java.util.Optional;

public class BudgetRevenueDynamoDbIdFactory implements BudgetDynamoDbIdFactory<BudgetRevenueId, BudgetRevenue> {

    private final static Base64.Encoder ENCODER = Base64.getEncoder();

    private final SaltGenerator saltGenerator;

    public BudgetRevenueDynamoDbIdFactory(SaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }


    @Override
    public BudgetRevenueId budgetIdFrom(BudgetRevenue budgetRevenue) {
        return Optional.ofNullable(budgetRevenue.getId())
                .orElseGet(() -> new BudgetRevenueId(String.format("%s-%s", partitionKeyFrom(budgetRevenue.getRegistrationDate(), new UserName(budgetRevenue.getUserName())), rangeKeyFrom(budgetRevenue))));
    }


    @Override
    public String partitionKeyFrom(Date date, UserName userName) {
        int budgetExpenseYear = date.getLocalDate().getYear();
        String budgetRevenueUser = userName.getContent();

        String partitionKey = String.format("%s_%s", budgetExpenseYear, budgetRevenueUser);

        return ENCODER.encodeToString(partitionKey.getBytes());
    }


    @Override
    public String partitionKeyFrom(BudgetRevenueId id) {
        return id.content().split("-")[0];
    }


    @Override
    public String rangeKeyFrom(BudgetRevenueId id) {
        return id.content().split("-")[1];
    }


    private String rangeKeyFrom(BudgetRevenue budgetRevenue) {
        int dayOfMonth = budgetRevenue.getRegistrationDate().getLocalDate().getDayOfMonth();
        int budgetRevenueMonth = budgetRevenue.getRegistrationDate().getLocalDate().getMonthValue();
        String salt = saltGenerator.newSalt();

        String rangeKey = String.format("%s_%s_%s", budgetRevenueMonth, dayOfMonth, salt);

        return ENCODER.encodeToString(rangeKey.getBytes());
    }
}