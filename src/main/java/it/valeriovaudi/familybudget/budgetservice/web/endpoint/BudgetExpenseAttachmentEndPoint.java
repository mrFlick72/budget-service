package it.valeriovaudi.familybudget.budgetservice.web.endpoint;


import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.CreateBudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.DeleteBudgetExpenseAttachment;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.GetBudgetExpenseAttachment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/budget/expense/{budgetExpenseId}/attachment")
public class BudgetExpenseAttachmentEndPoint {

    private final CreateBudgetExpense createBudgetExpense;
    private final GetBudgetExpenseAttachment getBudgetExpenseAttachment;
    private final DeleteBudgetExpenseAttachment deleteBudgetExpenseAttachment;


    public BudgetExpenseAttachmentEndPoint(CreateBudgetExpense createBudgetExpense,
                                           GetBudgetExpenseAttachment getBudgetExpenseAttachment,
                                           DeleteBudgetExpenseAttachment deleteBudgetExpenseAttachment) {
        this.createBudgetExpense = createBudgetExpense;
        this.getBudgetExpenseAttachment = getBudgetExpenseAttachment;
        this.deleteBudgetExpenseAttachment = deleteBudgetExpenseAttachment;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity newBudgetExpenseWithAttachment(@PathVariable("budgetExpenseId") String budgetExpenseId,
                                                         @RequestParam("attachment") MultipartFile attachmentFile) {
        createBudgetExpense.newBudgetExpenseAttachment(new BudgetExpenseId(budgetExpenseId), getAttachmentFor(attachmentFile));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/{attachmentFileName}/name")
    public ResponseEntity getAttachment(@PathVariable("budgetExpenseId") String budgetExpenseId,
                                        @PathVariable("attachmentFileName") String attachmentFileName) {

        return getBudgetExpenseAttachment.findAttachmentFor(new BudgetExpenseId(budgetExpenseId),
                new AttachmentFileName(attachmentFileName))
                .map(this::getFile)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping(value = "/{attachmentFileName}/name")
    public ResponseEntity deleteAttachment(@PathVariable("budgetExpenseId") String budgetExpenseId,
                                           @PathVariable("attachmentFileName") String attachmentFileName) {

        deleteBudgetExpenseAttachment.deleteAttachmentFor(new BudgetExpenseId(budgetExpenseId), new AttachmentFileName(attachmentFileName));
        return ResponseEntity.noContent().build();
    }

    private Attachment getAttachmentFor(@RequestParam("attachment") MultipartFile attachmentFile) {
        return new Attachment(new AttachmentFileName(attachmentFile.getOriginalFilename()),
                attachmentFile.getContentType(), getBytes(attachmentFile));
    }

    private byte[] getBytes(MultipartFile attachments) {
        try {
            return attachments.getBytes();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return new byte[0];
    }

    private ResponseEntity<byte[]> getFile(Attachment attachment) {
        byte[] content = attachment.getContent();

        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", attachment.getContentType());
        header.set("Content-Disposition", String.format("inline; filename=%s", attachment.getName().getFileName()));
        header.setContentLength(content.length);

        return ResponseEntity.ok().headers(header).body(content);
    }
}