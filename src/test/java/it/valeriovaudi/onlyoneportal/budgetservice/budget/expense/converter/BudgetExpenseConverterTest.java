package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint.BudgetExpenseRepresentation;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BudgetExpenseConverterTest {

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
        BudgetExpenseRepresentation budgetExpenseRepresentation = new BudgetExpenseRepresentation(id.getContent(), DATE, AMOUNT, "super-market", "super-market", "Super Market");
        BudgetExpenseConverter budgetExpenseConverter = new BudgetExpenseConverter(searchTagRepository, userRepository);

        given(userRepository.currentLoggedUserName())
                .willReturn(USER);

        BudgetExpense actualBudgetExpense = budgetExpenseConverter.representationModelToDomainModel(budgetExpenseRepresentation);
        verify(userRepository).currentLoggedUserName();

        Assertions.assertEquals(actualBudgetExpense, new BudgetExpense(id, USER, DOMAIN_DATE, MONEY_AMOUNT, "super-market", "super-market"));
    }

    @Test
    public void convertDomainModelToWebRepresentation() {
        BudgetExpenseId id = BudgetExpenseId.randomBudgetExpenseId();

        BudgetExpense budgetExpense = new BudgetExpense(id, USER, DOMAIN_DATE, MONEY_AMOUNT, "Super Market", "super-market");
        BudgetExpenseConverter budgetExpenseConverter = new BudgetExpenseConverter(searchTagRepository, userRepository);

        given(searchTagRepository.findSearchTagBy("super-market"))
                .willReturn(new SearchTag("super-market", "Super Market"));

        BudgetExpenseRepresentation actualBudgetExpenseRepresentation =
                budgetExpenseConverter.domainToRepresentationModel(budgetExpense);

        verify(searchTagRepository).findSearchTagBy("super-market");

        Assertions.assertEquals(actualBudgetExpenseRepresentation, new BudgetExpenseRepresentation(id.getContent(), DATE, AMOUNT, "Super Market", "super-market", "Super Market"));
    }

}