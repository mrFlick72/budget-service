package it.valeriovaudi.familybudget.budgetservice.web.adapter;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.UserRepository;
import it.valeriovaudi.familybudget.budgetservice.web.model.BudgetExpenseRepresentation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BudgetExpenseAdapterTest {

    private static final String AMOUNT = "12.50";
    private static final Money MONEY_AMOUNT = Money.moneyFor("12.50");
    private static final String DATE = "25/02/2018";
    private static final Date DOMAIN_DATE = Date.dateFor("25/02/2018");
    private final UserName USER = new UserName("USER");

    @Mock
    private SearchTagRepository searchTagRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void convertWebRepresentationToDomainModel() {
        BudgetExpenseId id = BudgetExpenseId.randomBudgetExpenseId();
        BudgetExpenseRepresentation budgetExpenseRepresentation = new BudgetExpenseRepresentation(id.getContent(), DATE, AMOUNT, "super-market", "super-market", "Super Market", asList());
        BudgetExpenseAdapter budgetExpenseAdapter = new BudgetExpenseAdapter(searchTagRepository, userRepository);

        given(userRepository.currentLoggedUserName())
                .willReturn(USER);

        BudgetExpense actualBudgetExpense = budgetExpenseAdapter.representationModelToDomainModel(budgetExpenseRepresentation);
        verify(userRepository).currentLoggedUserName();

        assertThat(actualBudgetExpense, is(new BudgetExpense(id, USER, DOMAIN_DATE, MONEY_AMOUNT, "super-market", "super-market")));
    }

    @Test
    public void convertDomainModelToWebRepresentation() {
        BudgetExpenseId id = BudgetExpenseId.randomBudgetExpenseId();

        BudgetExpense budgetExpense = new BudgetExpense(id, USER, DOMAIN_DATE, MONEY_AMOUNT, "Super Market", "super-market");
        BudgetExpenseAdapter budgetExpenseAdapter = new BudgetExpenseAdapter(searchTagRepository, userRepository);

        given(searchTagRepository.findSearchTagBy("super-market"))
                .willReturn(new SearchTag("super-market", "Super Market"));

        BudgetExpenseRepresentation actualBudgetExpenseRepresentation =
                budgetExpenseAdapter.domainToRepresentationModel(budgetExpense);

        verify(searchTagRepository).findSearchTagBy("super-market");

        assertThat(actualBudgetExpenseRepresentation, is(new BudgetExpenseRepresentation(id.getContent(), DATE, AMOUNT, "Super Market", "super-market", "Super Market", asList())));
    }

}