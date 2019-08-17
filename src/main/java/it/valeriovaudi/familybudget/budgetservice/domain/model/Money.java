package it.valeriovaudi.familybudget.budgetservice.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
public final class Money {

    static final int SCALE_PRECISION                = 2;
    static final int SCALE_CRITERIA                 = BigDecimal.ROUND_HALF_DOWN;

    public static final Money ZERO = Money.moneyFor("0.00");
    public static final Money ONE = Money.moneyFor("1.00");

    private final BigDecimal amount;


    public Money(BigDecimal amount) {
        this.amount = amount.setScale(SCALE_PRECISION, SCALE_CRITERIA);
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public static Money moneyFor(String amount){
        return new Money(new BigDecimal(amount));
    }

    public Money plus(Money money){
        return new Money(this.amount.add(money.getAmount()).setScale(SCALE_PRECISION, SCALE_CRITERIA));
    }

    public String stringifyAmount(){
        return amount.toString();
    }
}
