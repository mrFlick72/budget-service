package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import java.util.UUID;

public class UUIDSaltGenerator implements SaltGenerator {

    @Override
    public String newSalt() {
        return UUID.randomUUID().toString();
    }
}
