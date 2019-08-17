package it.valeriovaudi.familybudget.budgetservice.domain.repository;

import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;

public interface UserRepository {

    UserName currentLoggedUserName();
}
