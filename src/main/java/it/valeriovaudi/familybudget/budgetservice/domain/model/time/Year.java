package it.valeriovaudi.familybudget.budgetservice.domain.model.time;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public final class Year {

    private final Integer yearValue;

    Year(Integer yearValue) {
        this.yearValue = yearValue;
    }

    public static Year of(Integer year) {
        return new Year(year);
    }
    public static Year now() {
        return new Year(LocalDate.now().getYear());
    }
}
