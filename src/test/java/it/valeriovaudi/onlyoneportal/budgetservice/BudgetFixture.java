package it.valeriovaudi.onlyoneportal.budgetservice;


import it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.SaltGenerator;

import java.util.UUID;

public class BudgetFixture {

    public static final String SALT = UUID.randomUUID().toString();
    public static final SaltGenerator saltGenerator = () -> SALT;

}
