package it.valeriovaudi.familybudget.budgetservice.web.converter;

import it.valeriovaudi.familybudget.budgetservice.web.model.BudgetSearchCriteriaRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;

public class StringToSearchCriteriaRepresentationConverterTest {

    @Test
    public void convert() {
        StringToBudgetSearchCriteriaRepresentationConverter converter =
                new StringToBudgetSearchCriteriaRepresentationConverter();

        Assertions.assertEquals(converter.convert("month=1;year=2018;searchTag="), new BudgetSearchCriteriaRepresentation(1, 2018, emptyList()));
    }

}
