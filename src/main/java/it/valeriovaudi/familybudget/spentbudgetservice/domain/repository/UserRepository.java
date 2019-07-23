package it.valeriovaudi.familybudget.spentbudgetservice.domain.repository;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;

public interface UserRepository {

    UserName currentLoggedUserName();
}
