package it.valeriovaudi.familybudget.budgetservice.web.converter;

import it.valeriovaudi.familybudget.budgetservice.web.model.BudgetSearchCriteriaRepresentation;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringToSearchCriteriaRepresentationConverterTest {

    @Test
    public void convert() {
        StringToBudgetSearchCriteriaRepresentationConverter converter =
                new StringToBudgetSearchCriteriaRepresentationConverter();

        assertThat(converter.convert("month=1;year=2018;searchTag="), is(new BudgetSearchCriteriaRepresentation(1, 2018, emptyList())));
    }

}
