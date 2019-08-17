package it.valeriovaudi.familybudget.budgetservice.domain.model.time;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
public final class Month {

    public static final Month JANUARY = new Month(1);
    public static final Month FEBRUARY = new Month(2);
    public static final Month MARCH = new Month(3);
    public static final Month APRIL = new Month(4);
    public static final Month MAY = new Month(5);
    public static final Month JUNE = new Month(6);
    public static final Month JULY = new Month(7);
    public static final Month AUGUST = new Month(8);
    public static final Month SEPTEMBER = new Month(9);
    public static final Month OCTOBER = new Month(10);
    public static final Month NOVEMBER = new Month(11);
    public static final Month DECEMBER = new Month(12);

    private final Integer monthValue;

    Month(@NotNull Integer monthValue) {
        this.monthValue = monthValue;
    }

    public static Month of(@NotNull Integer monthValue) {
        return new Month(monthValue);
    }

    public String localizedMonthName(Locale locale) {
        return java.time.Month.of(monthValue).getDisplayName(TextStyle.FULL,
                Optional.ofNullable(locale).orElse(Locale.ENGLISH));
    }

    public static Month now() {
        return new Month(LocalDate.now().getMonthValue());
    }

    public Integer monthValue() {
        return monthValue;
    }
}