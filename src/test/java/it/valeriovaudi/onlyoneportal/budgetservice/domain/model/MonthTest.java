package it.valeriovaudi.onlyoneportal.budgetservice.domain.model;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class MonthTest {


    @Test
    public void getMonthName() {
        String actual = Month.FEBRUARY.localizedMonthName(Locale.ENGLISH);
        String expected = "February";

        Assertions.assertEquals(actual, expected);
    }

}