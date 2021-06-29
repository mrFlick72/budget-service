package it.valeriovaudi.onlyoneportal.budgetservice.domain.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;

public interface UserRepository {

    UserName currentLoggedUserName();
}
