package it.valeriovaudi.onlyoneportal.budgetservice.web.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.budget.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Year;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.usecase.FindBudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.web.adapter.BudgetRevenueAdapter;
import it.valeriovaudi.onlyoneportal.budgetservice.web.model.BudgetRevenueRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static it.valeriovaudi.onlyoneportal.budgetservice.domain.model.time.Date.dateFor;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BudgetRevenueEndPoint.class)
public class BudgetRevenueEndPointTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private JwtDecoder decoder;

    @MockBean
    private BudgetRevenueRepository budgetRevenueRepository;

    @MockBean
    private FindBudgetRevenue findBudgetRevenue;

    @MockBean
    private IdProvider idProvider;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BudgetRevenueAdapter budgetRevenueAdapter;

    @Test
    @WithMockUser("USER")
    public void addANewBudgetRevenue() throws Exception {

        String mockedId = UUID.randomUUID().toString();

        BudgetRevenue budgetRevenue = new BudgetRevenue(mockedId, "USER", dateFor("10/10/2018"), Money.ONE, "A_NOTE");

        given(idProvider.id()).willReturn(mockedId);
        given(userRepository.currentLoggedUserName()).willReturn(new UserName("USER"));

        BudgetRevenueRepresentation budgetRevenueRepresentation = new BudgetRevenueRepresentation(null, "10/10/2018", "1.00", "A_NOTE");
        mockMvc.perform(post("/budget/revenue")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsBytes(budgetRevenueRepresentation)))
                .andExpect(status().isCreated());

        verify(budgetRevenueRepository).save(budgetRevenue);
        verify(userRepository).currentLoggedUserName();
    }

    @Test
    @WithMockUser("USER")
    public void updateABudgetRevenue() throws Exception {
        String mockedId = UUID.randomUUID().toString();

        BudgetRevenue budgetRevenue = new BudgetRevenue(mockedId, "USER", dateFor("10/10/2018"), Money.ONE, "A_NOTE");
        BudgetRevenueRepresentation budgetRevenueRepresentation = new BudgetRevenueRepresentation(mockedId, "10/10/2018", "1.00", "A_NOTE");

        given(budgetRevenueAdapter.fromRepresentationToModel(budgetRevenueRepresentation))
                .willReturn(budgetRevenue);

        mockMvc.perform(put("/budget/revenue/" + mockedId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsBytes(budgetRevenueRepresentation)))
                .andExpect(status().isNoContent());

        verify(budgetRevenueRepository).update(budgetRevenue);
    }

    @Test
    @WithMockUser("USER")
    public void deleteABudgetRevenue() throws Exception {
        String mockedId = UUID.randomUUID().toString();

        mockMvc.perform(delete("/budget/revenue/" + mockedId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(budgetRevenueRepository).delete(mockedId);
    }

    @Test
    @WithMockUser("USER")
    public void findAllBudgetRevenue() throws Exception {
        Year year = Year.of(2018);

        BudgetRevenue aBudgetRevenue = new BudgetRevenue("AN_ID", "USER", dateFor("05/01/2018"), Money.ONE, "A_NOTE");
        BudgetRevenue anotherBudgetRevenue = new BudgetRevenue("AN_OTHER_ID", "USER", dateFor("15/01/2018"), Money.ONE, "");


        List<BudgetRevenue> budgetRevenueList = asList(aBudgetRevenue, anotherBudgetRevenue);
        given(findBudgetRevenue.findBy(year))
                .willReturn(budgetRevenueList);

        given(budgetRevenueAdapter.fromDomainToRepresentation(aBudgetRevenue))
                .willReturn(new BudgetRevenueRepresentation("AN_ID", "05/01/2018", "1.00", "A_NOTE"));

        given(budgetRevenueAdapter.fromDomainToRepresentation(anotherBudgetRevenue))
                .willReturn(new BudgetRevenueRepresentation("AN_OTHER_ID", "15/01/2018", "1.00", ""));


        mockMvc.perform(get("/budget/revenue?q=year=2018")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(loadJson("budget_revenue/findAll.json")));

        verify(findBudgetRevenue).findBy(year);
        verify(budgetRevenueAdapter).fromDomainToRepresentation(aBudgetRevenue);
        verify(budgetRevenueAdapter).fromDomainToRepresentation(anotherBudgetRevenue);
    }

    public String loadJson(String stub) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(stub)) {
            return new String(inputStream.readAllBytes());
        }
    }

}