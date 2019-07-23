package it.valeriovaudi.familybudget.spentbudgetservice.domain.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MoneyTest {

    @Test
    public void manoeyScaleIsCorrect() throws Exception {
        Money money = Money.moneyFor("12.506");
        BigDecimal expectedValue = new BigDecimal(12.51).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal actualValue = money.getAmount();
        assertThat(actualValue, is(expectedValue));
    }

    @Test
    public void addOperation() throws Exception {
        Money firstAddendum = Money.moneyFor("12.50");
        Money secondAddendum = Money.moneyFor("10.22");

        Money expectedValue = Money.moneyFor("22.72");

        assertThat(firstAddendum.plus(secondAddendum), is(expectedValue));

    }
}