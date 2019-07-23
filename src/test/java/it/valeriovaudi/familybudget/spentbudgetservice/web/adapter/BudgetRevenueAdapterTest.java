package it.valeriovaudi.familybudget.spentbudgetservice.web.adapter;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.UserRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.web.model.BudgetRevenueRepresentation;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BudgetRevenueAdapterTest {

    @Mock
    private UserRepository userRepository;

    private BudgetRevenueAdapter budgetRevenueAdapter;

    @Before
    public void setUp() {
        budgetRevenueAdapter = new BudgetRevenueAdapter(userRepository);

    }

    @Test
    public void fromRepresentationToModelHappyPath() {
        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("USER"));

        BudgetRevenue actual = budgetRevenueAdapter.fromRepresentationToModel(new BudgetRevenueRepresentation("AN_ID", "01/01/2018", "1.00", "A_NOTE"));
        BudgetRevenue expected = new BudgetRevenue("AN_ID", "USER", Date.dateFor("01/01/2018"), Money.ONE, "A_NOTE");
        Assert.assertThat(actual, Is.is(expected));

        verify(userRepository).currentLoggedUserName();
    }

    @Test
    public void fromDomainToRepresentationHappyPath() {
        BudgetRevenueRepresentation actual = budgetRevenueAdapter.fromDomainToRepresentation(new BudgetRevenue("AN_ID", "USER", Date.dateFor("01/01/2018"), Money.ONE, "A_NOTE"));
        BudgetRevenueRepresentation expected = new BudgetRevenueRepresentation("AN_ID", "01/01/2018", "1.00", "A_NOTE");
        Assert.assertThat(actual, Is.is(expected));
    }
}