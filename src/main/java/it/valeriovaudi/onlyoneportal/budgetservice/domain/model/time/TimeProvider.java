package it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time;

public class TimeProvider {

    public Month getCurrentMonth(){
        return Month.now();
    }

    public Year getCurrentYear(){
        return Year.now();
    }
}
