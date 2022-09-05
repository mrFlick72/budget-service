package it.valeriovaudi.onlyoneportal.budgetservice;


import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.SaltGenerator;


public class BudgetFixture {

    public static final String SALT = "A_SALT";
    public static final SaltGenerator saltGenerator = () -> SALT;

}
