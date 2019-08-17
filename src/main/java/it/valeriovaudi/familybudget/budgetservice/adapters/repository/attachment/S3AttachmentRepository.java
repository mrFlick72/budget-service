package it.valeriovaudi.familybudget.budgetservice.adapters.repository.attachment;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.AttachmentRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

@Slf4j
public class S3AttachmentRepository implements AttachmentRepository {

    private static final String BUDGET_EXPENSE_ID = "budget-expense-id";
    private static final String BUDGET_EXPENSE_DATE = "budget-expense-date";
    private static final String BUDGET_EXPENSE_SEARCH_TAG = "budget-expense-search-tag";

    private final S3AttachmentPathProvider s3AttachmentPathProvider;
    private final AmazonS3 s3client;
    private final String bucketName;


    public S3AttachmentRepository(AmazonS3 s3client,
                                  String bucketName,
                                  String bucketAttachmentPrefixKey) {

        this.s3AttachmentPathProvider = new S3AttachmentPathProvider(bucketAttachmentPrefixKey);
        this.s3client = s3client;
        this.bucketName = bucketName;
    }

    @Override
    public void save(BudgetExpense budgetExpense, Attachment attachment) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(attachment.getContent())) {
            ObjectMetadata objectMetadata = getObjectMetadataFor(budgetExpense, attachment);
            s3client.putObject(bucketName, objectKeyFor(budgetExpense, attachment.getName()), input, objectMetadata);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String objectKeyFor(BudgetExpense budgetExpense, AttachmentFileName attachment) {
        return s3AttachmentPathProvider.provide(budgetExpense, attachment);
    }

    private ObjectMetadata getObjectMetadataFor(BudgetExpense budgetExpense, Attachment attachment) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.addUserMetadata(BUDGET_EXPENSE_ID, budgetExpense.getId().getContent());
        objectMetadata.addUserMetadata(BUDGET_EXPENSE_DATE, budgetExpense.getDate().formattedDate());
        objectMetadata.addUserMetadata(BUDGET_EXPENSE_SEARCH_TAG, budgetExpense.getTag());
        objectMetadata.setContentType(attachment.getContentType());
        objectMetadata.setContentLength(attachment.getContent().length);
        return objectMetadata;
    }

    @Override
    public List<AttachmentFileName> findFor(BudgetExpense budgetExpense) {
        String attachmentDatePath = objectKeyFor(budgetExpense, AttachmentFileName.emptyFileName());
        ObjectListing objectListing = s3client.listObjects(bucketName, attachmentDatePath);
        return objectListing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .map(this::getAttachmentFileNameFor)
                .map(AttachmentFileName::new)
                .collect(toList());
    }

    @Override
    public Optional<Attachment> findAttachmentFor(BudgetExpense budgetExpense, AttachmentFileName attachmentFileName) {
        String attachmentDatePath = objectKeyFor(budgetExpense, attachmentFileName);
        return Optional.ofNullable(getAttachmentFileOnS3For(attachmentDatePath))
                .map(s3Object -> new Attachment(attachmentFileName, s3Object.getObjectMetadata().getContentType(), getContent(s3Object)));
    }

    @Override
    public void delete(BudgetExpense budgetExpense, AttachmentFileName attachmentFileName) {
        String attachmentDatePath = objectKeyFor(budgetExpense, attachmentFileName);
        try {
            s3client.deleteObject(bucketName, attachmentDatePath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private S3Object getAttachmentFileOnS3For(String attachmentDatePath) {
        S3Object object = null;
        try {
            object = s3client.getObject(bucketName, attachmentDatePath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return object;
    }

    private byte[] getContent(S3Object s3Object) {
        try {
            return s3Object.getObjectContent().readAllBytes();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new byte[0];
    }

    private String getAttachmentFileNameFor(String s3Key) {
        String[] split = s3Key.split("/");
        return split[split.length - 1];
    }

}
