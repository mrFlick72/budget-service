package it.valeriovaudi.familybudget.spentbudgetservice.domain.model;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Month;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MonthTest {


    @Test
    public void getMonthName(){
        String actual = Month.FEBRUARY.localizedMonthName(Locale.ENGLISH);
        String expected = "February";

        assertThat(actual, is(expected));
    }

}