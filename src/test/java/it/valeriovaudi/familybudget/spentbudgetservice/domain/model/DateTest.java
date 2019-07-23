package it.valeriovaudi.familybudget.spentbudgetservice.domain.model;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Year;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DateTest
{
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-YYY");

    @Test
    public void dateIsFormattedWithCustomFormatter(){
        String expectedFormattedDate = "25-02-2018";
        String anotherExpectedFormattedDate = "25-03-2018";
        String anotherExpectedFormattedDate2 = "25-05-2018";

        Date date = new Date(LocalDate.of(2018, 02, 25), DATE_TIME_FORMATTER);
        Date anotherDate = new Date(LocalDate.of(2018, 03, 25), DATE_TIME_FORMATTER);
        Date anotherDate2 = new Date(LocalDate.of(2018, 05, 25), DATE_TIME_FORMATTER);

        assertThat(date.formattedDate(), is(expectedFormattedDate));
        assertThat(anotherDate.formattedDate(), is(anotherExpectedFormattedDate));
        assertThat(anotherDate2.formattedDate(), is(anotherExpectedFormattedDate2));
    }


    @Test
    public void dateIsFormattedWithDefaultFormatter(){
        String expectedFormattedDate = "25/02/2018";
        String anotherExpectedFormattedDate = "25/03/2018";
        String anotherExpectedFormattedDate2 = "25/05/2018";

        Date date = new Date(LocalDate.of(2018, 02, 25));
        Date anotherDate = new Date(LocalDate.of(2018, 03, 25));
        Date anotherDate2 = new Date(LocalDate.of(2018, 05, 25));

        assertThat(date.formattedDate(), is(expectedFormattedDate));
        assertThat(anotherDate.formattedDate(), is(anotherExpectedFormattedDate));
        assertThat(anotherDate2.formattedDate(), is(anotherExpectedFormattedDate2));
    }

    @Test
    public void dateFromString() {
        Date expectedDateForDateString = new Date(LocalDate.of(2018,02,25));
        Date actualDateForDateString = Date.dateFor("25/02/2018");
        assertThat(actualDateForDateString, is(expectedDateForDateString));
    }

    @Test
    public void firstDateOfMonth() {
        Date expected = Date.dateFor("01/02/2018");
        Date actual = Date.firstDateOfMonth(Month.FEBRUARY, Year.of(2018));

        assertThat(actual, is(expected));
    }

    @Test
    public void lastDateOfMonth() {
        Date expected = Date.dateFor("28/02/2018");
        Date actual = Date.lastDateOfMonth(Month.FEBRUARY, Year.of(2018));

        assertThat(actual, is(expected));
    }
}