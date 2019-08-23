package it.valeriovaudi.familybudget.budgetservice.web.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.SearchTagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchTagEndPoint.class)
public class SearchTagEndPointTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private JwtDecoder decoder;

    @MockBean
    private SearchTagRepository searchTagRepository;

    @Test
    @WithMockUser
    public void getAllSearchTag() throws Exception {

        List<SearchTag> expectedValue = asList(new SearchTag("key", "value"), new SearchTag("key", "value"), new SearchTag("key", "value"));
        given(searchTagRepository.findAllSearchTag())
                .willReturn(expectedValue);
        String expected = objectMapper.writeValueAsString(expectedValue);
        mockMvc.perform(get("/budget-expense/search-tag")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    @WithMockUser
    public void newSearchTag() throws Exception {
        SearchTag searchTag = new SearchTag("key", "value");
        String content = objectMapper.writeValueAsString(searchTag);

        mockMvc.perform(put("/budget-expense/search-tag")
                .content(content)
                .contentType("application/json")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(searchTagRepository).save(searchTag);
    }

    @Test
    @WithMockUser
    public void deleteSearchTag() throws Exception {
        mockMvc.perform(delete("/budget-expense/search-tag/key")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(searchTagRepository).delete("key");
    }
}
