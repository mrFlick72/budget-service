package it.valeriovaudi.onlyoneportal.budgetservice.support;


import it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb.SaltGenerator;


public class BudgetFixture {

    public static final String SALT = "A_SALT";
    public static final SaltGenerator saltGenerator = () -> SALT;

}
