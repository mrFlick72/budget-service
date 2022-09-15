package it.valeriovaudi.onlyoneportal.budgetservice.web.adapter;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueId;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.web.model.BudgetRevenueRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BudgetRevenueAdapterTest {

    @Mock
    private UserRepository userRepository;

    private BudgetRevenueAdapter budgetRevenueAdapter;

    @BeforeEach
    public void setUp() {
        budgetRevenueAdapter = new BudgetRevenueAdapter(userRepository);

    }

    @Test
    public void fromRepresentationToModelHappyPath() {
        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("USER"));

        BudgetRevenue actual = budgetRevenueAdapter.fromRepresentationToModel(new BudgetRevenueRepresentation("AN_ID", "01/01/2018", "1.00", "A_NOTE"));
        BudgetRevenue expected = new BudgetRevenue("AN_ID", new BudgetRevenueId("AN_ID"), "USER", Date.dateFor("01/01/2018"), Money.ONE, "A_NOTE");
        Assertions.assertEquals(actual, expected);

        verify(userRepository).currentLoggedUserName();
    }

    @Test
    public void fromDomainToRepresentationHappyPath() {
        BudgetRevenueRepresentation actual = budgetRevenueAdapter.fromDomainToRepresentation(new BudgetRevenue("AN_ID",  new BudgetRevenueId("AN_ID"), "USER", Date.dateFor("01/01/2018"), Money.ONE, "A_NOTE"));
        BudgetRevenueRepresentation expected = new BudgetRevenueRepresentation("AN_ID", "01/01/2018", "1.00", "A_NOTE");
        Assertions.assertEquals(actual, expected);
    }
}