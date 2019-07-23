package it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time;

public class TimeProvider {

    public Month getCurrentMonth(){
        return Month.now();
    }

    public Year getCurrentYear(){
        return Year.now();
    }
}
