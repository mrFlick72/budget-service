package it.valeriovaudi.familybudget.budgetservice.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class SearchTag {

    private final String key;
    private final String value;

    public static final String DEFAULT_KEY = "unknown";

    public SearchTag(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
