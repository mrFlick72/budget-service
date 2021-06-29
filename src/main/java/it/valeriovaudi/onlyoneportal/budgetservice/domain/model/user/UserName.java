package it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class UserName {

    private final String content;

    public UserName(String content) {
        this.content = content;
    }
}
