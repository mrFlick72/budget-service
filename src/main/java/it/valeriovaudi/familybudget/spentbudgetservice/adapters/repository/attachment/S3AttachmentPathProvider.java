package it.valeriovaudi.familybudget.spentbudgetservice.adapters.repository.attachment;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;

import java.time.LocalDate;

import static java.lang.String.join;

public class S3AttachmentPathProvider {
    private final String bucketAttachmentPrefixKey;

    public S3AttachmentPathProvider(String bucketAttachmentPrefixKey) {
        this.bucketAttachmentPrefixKey = bucketAttachmentPrefixKey;
    }

    public String provide(BudgetExpense budgetExpense, AttachmentFileName attachment) {
        return join("/", bucketAttachmentPrefixKey, attachmentDatePath(budgetExpense),
                budgetExpense.getId().getContent(), attachment.getFileName());
    }

    public String attachmentDatePath(BudgetExpense budgetExpense) {
        LocalDate localDate = budgetExpense.getDate().getLocalDate();
        return join("/", String.valueOf(localDate.getYear()),
                String.valueOf(localDate.getMonth().getValue()),
                String.valueOf(localDate.getDayOfMonth()));
    }
}
