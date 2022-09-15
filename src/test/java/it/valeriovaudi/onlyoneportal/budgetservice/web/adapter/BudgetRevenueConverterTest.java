package it.valeriovaudi.onlyoneportal.budgetservice.web.adapter;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueConverter;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueId;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueRepresentation;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BudgetRevenueConverterTest {

    @Mock
    private UserRepository userRepository;

    private BudgetRevenueConverter budgetRevenueConverter;

    @BeforeEach
    public void setUp() {
        budgetRevenueConverter = new BudgetRevenueConverter(userRepository);

    }

    @Test
    public void fromRepresentationToModelHappyPath() {
        given(userRepository.currentLoggedUserName())
                .willReturn(new UserName("USER"));

        BudgetRevenue actual = budgetRevenueConverter.fromRepresentationToModel(new BudgetRevenueRepresentation("AN_ID", "01/01/2018", "1.00", "A_NOTE"));
        BudgetRevenue expected = new BudgetRevenue(new BudgetRevenueId("AN_ID"), "USER", Date.dateFor("01/01/2018"), Money.ONE, "A_NOTE");
        Assertions.assertEquals(actual, expected);

        verify(userRepository).currentLoggedUserName();
    }

    @Test
    public void fromDomainToRepresentationHappyPath() {
        BudgetRevenueRepresentation actual = budgetRevenueConverter.fromDomainToRepresentation(new BudgetRevenue(new BudgetRevenueId("AN_ID"), "USER", Date.dateFor("01/01/2018"), Money.ONE, "A_NOTE"));
        BudgetRevenueRepresentation expected = new BudgetRevenueRepresentation("AN_ID", "01/01/2018", "1.00", "A_NOTE");
        Assertions.assertEquals(actual, expected);
    }
}