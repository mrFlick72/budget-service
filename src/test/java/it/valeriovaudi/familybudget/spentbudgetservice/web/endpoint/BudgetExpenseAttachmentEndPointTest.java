package it.valeriovaudi.familybudget.spentbudgetservice.web.endpoint;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase.CreateBudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase.DeleteBudgetExpenseAttachment;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase.GetBudgetExpenseAttachment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BudgetExpenseAttachmentEndPoint.class)
public class BudgetExpenseAttachmentEndPointTest {

    @MockBean
    private JwtDecoder decoder;

    @MockBean
    private CreateBudgetExpense createBudgetExpense;

    @MockBean
    private GetBudgetExpenseAttachment getBudgetExpenseAttachment;

    @MockBean
    private DeleteBudgetExpenseAttachment deleteBudgetExpenseAttachment;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void insertAttachment() throws Exception {
        String budgetExpenseId = UUID.randomUUID().toString();
        Attachment attachment = new Attachment(new AttachmentFileName("anAttachment.pdf"), "application/pdf", "attachments content".getBytes());

        mockMvc.perform(multipart(format("/budget/expense/%s/attachment", budgetExpenseId))
                .file(new MockMultipartFile("attachment", "anAttachment.pdf", "application/pdf", "attachments content".getBytes()))
                .with(csrf()))
                .andExpect(status().isCreated());

        verify(createBudgetExpense).newBudgetExpenseAttachment(new BudgetExpenseId(budgetExpenseId), attachment);
    }

    @Test
    @WithMockUser
    public void getAttachment() throws Exception {
        AttachmentFileName attachmentFileName = new AttachmentFileName("anAttachment.pdf");
        String contentType = "plain/text";
        Attachment attachment = new Attachment(attachmentFileName, contentType, "attachments content".getBytes());
        BudgetExpenseId budgetExpenseId = new BudgetExpenseId(UUID.randomUUID().toString());

        String endPointUrl = format("/budget/expense/%s/attachment/%s/name",
                budgetExpenseId.getContent(),
                attachmentFileName.getFileName());

        given(getBudgetExpenseAttachment.findAttachmentFor(budgetExpenseId, attachmentFileName))
                .willReturn(Optional.of(attachment));

        mockMvc.perform(get(endPointUrl)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", contentType))
                .andExpect(content().bytes("attachments content".getBytes()));

        verify(getBudgetExpenseAttachment).findAttachmentFor(budgetExpenseId, attachmentFileName);
    }

    @Test
    @WithMockUser
    public void deleteAnAttachment() throws Exception {
        AttachmentFileName attachmentFileName = new AttachmentFileName("anAttachment.pdf");
        BudgetExpenseId budgetExpenseId = new BudgetExpenseId(UUID.randomUUID().toString());

        String endPointUrl = format("/budget/expense/%s/attachment/%s/name",
                budgetExpenseId.getContent(),
                attachmentFileName.getFileName());

        mockMvc.perform(delete(endPointUrl)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(deleteBudgetExpenseAttachment).deleteAttachmentFor(budgetExpenseId, attachmentFileName);
    }
}