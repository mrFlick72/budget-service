package it.valeriovaudi.onlyoneportal.budgetservice.domain.model;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class SearchTag {

    private final UserName userName;
    private final String key;
    private final String value;

    public static final String DEFAULT_KEY = "unknown";

    public SearchTag(UserName userName, String key, String value) {
        this.userName = userName;
        this.key = key;
        this.value = value;
    }
}
