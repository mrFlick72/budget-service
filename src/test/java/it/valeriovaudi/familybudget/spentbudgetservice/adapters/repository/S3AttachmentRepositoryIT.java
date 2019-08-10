package it.valeriovaudi.familybudget.spentbudgetservice.adapters.repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.repository.attachment.S3AttachmentPathProvider;
import it.valeriovaudi.familybudget.spentbudgetservice.adapters.repository.attachment.S3AttachmentRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money.ONE;
import static it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date.firstDateOfMonth;
import static it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Month.JANUARY;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@Slf4j
public class S3AttachmentRepositoryIT {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final String CONTENT_TYPE = "application/pdf";
    private static final String BUCKET_NAME = "family-budget-data";
    private static final String CONTENT_KEY_PREFIX = "dev";

    private static final String BUDGET_EXPENSE_ID = "budget-expense-id";
    private static final String BUDGET_EXPENSE_DATE = "budget-expense-date";
    private static final String BUDGET_EXPENSE_SEARCH_TAG = "budget-expense-search-tag";
    private static final byte[] MESSAGE = "hello World".getBytes();
    private S3AttachmentRepository s3AttachmentRepository;
    private AmazonS3 s3client;

    @Before
    public void setUp() {
        AWSCredentials credentials =
                new BasicAWSCredentials("your access key", "your secret key");

        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();

        s3AttachmentRepository = new S3AttachmentRepository(s3client, BUCKET_NAME, CONTENT_KEY_PREFIX);
    }

    @Test
    public void saveANewAttachment() throws IOException {
        byte[] bytes = new ClassPathResource("schema.sql").getInputStream().readAllBytes();
        BudgetExpenseId budgetExpenseId = BudgetExpenseId.randomBudgetExpenseId();
        Attachment attachment = new Attachment(new AttachmentFileName("AN_ATTACHMENT_NAME"), CONTENT_TYPE, bytes);
        BudgetExpense budgetExpense = new BudgetExpense(budgetExpenseId,
                new UserName("USER"),
                firstDateOfMonth(JANUARY, Year.of(2018)),
                ONE, "A_NOTE", "A_TAG");
        s3AttachmentRepository.save(budgetExpense, attachment);

        S3AttachmentPathProvider s3AttachmentPathProvider = new S3AttachmentPathProvider(CONTENT_KEY_PREFIX);
        S3Object object = s3client.getObject(BUCKET_NAME, s3AttachmentPathProvider.provide(budgetExpense, attachment.getName()));
        assertNotNull(object);

        log.info(object.toString());

        assertThat(new BudgetExpenseId(object.getObjectMetadata().getUserMetaDataOf(BUDGET_EXPENSE_ID)), is(budgetExpenseId));
        assertThat(object.getObjectMetadata().getUserMetaDataOf(BUDGET_EXPENSE_DATE), is(budgetExpense.getDate().formattedDate()));
        assertThat(object.getObjectMetadata().getUserMetaDataOf(BUDGET_EXPENSE_SEARCH_TAG), is(budgetExpense.getTag()));
    }

    @Test
    public void findAllAttachmentsForABudgetExpense() {
        BudgetExpenseId budgetExpenseId = BudgetExpenseId.randomBudgetExpenseId();
        Attachment anAttachment = new Attachment(new AttachmentFileName("AN_ATTACHMENT_NAME"), CONTENT_TYPE, MESSAGE);
        Attachment anotherAttachment = new Attachment(new AttachmentFileName("ANOTHER_ATTACHMENT_NAME"), CONTENT_TYPE, MESSAGE);
        Attachment anotherAttachmentAgain = new Attachment(new AttachmentFileName("ANOTHER_ATTACHMENT_NAME_AGAIN"), CONTENT_TYPE, MESSAGE);
        BudgetExpense budgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), firstDateOfMonth(JANUARY, Year.of(2018)),
                ONE, "A_NOTE", "A_TAG");

        s3AttachmentRepository.save(budgetExpense, anAttachment);
        s3AttachmentRepository.save(budgetExpense, anotherAttachment);
        s3AttachmentRepository.save(budgetExpense, anotherAttachmentAgain);

        List<AttachmentFileName> attachmentFileNames = s3AttachmentRepository.findFor(budgetExpense);

        log.info(attachmentFileNames.toString());
        assertThat(attachmentFileNames, containsInAnyOrder(anAttachment.getName(),
                anotherAttachment.getName(), anotherAttachmentAgain.getName()));
    }

    @Test
    public void findAllAttachmentsForABudgetExpenseWhenBudgetExpenseDoesNotExist() {
        BudgetExpenseId budgetExpenseId = BudgetExpenseId.randomBudgetExpenseId();
        BudgetExpense budgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), firstDateOfMonth(JANUARY, Year.of(2018)),
                ONE, "A_NOTE", "A_TAG");

        List<AttachmentFileName> attachmentFileNames = s3AttachmentRepository.findFor(budgetExpense);

        log.info(attachmentFileNames.toString());
        assertTrue(attachmentFileNames.isEmpty());
    }

    @Test
    public void findAttachmentsForABudgetExpense() {
        BudgetExpenseId budgetExpenseId = BudgetExpenseId.randomBudgetExpenseId();

        Attachment anAttachment = new Attachment(new AttachmentFileName("AN_ATTACHMENT_NAME"), CONTENT_TYPE, MESSAGE);
        Attachment anotherAttachment = new Attachment(new AttachmentFileName("ANOTHER_ATTACHMENT_NAME"), CONTENT_TYPE, MESSAGE);
        Attachment anotherAttachmentAgain = new Attachment(new AttachmentFileName("ANOTHER_ATTACHMENT_NAME_AGAIN"), CONTENT_TYPE, MESSAGE);
        BudgetExpense budgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), firstDateOfMonth(JANUARY, Year.of(2018)),
                ONE, "A_NOTE", "A_TAG");

        s3AttachmentRepository.save(budgetExpense, anAttachment);
        s3AttachmentRepository.save(budgetExpense, anotherAttachment);
        s3AttachmentRepository.save(budgetExpense, anotherAttachmentAgain);

        Optional<Attachment> actual = s3AttachmentRepository.findAttachmentFor(budgetExpense, new AttachmentFileName("AN_ATTACHMENT_NAME"));
        assertThat(actual, is(of(anAttachment)));
    }

    @Test
    public void findAttachmentsForABudgetExpenseWhenBudgetExpenseDoesNotExist() {
        BudgetExpenseId budgetExpenseId = BudgetExpenseId.randomBudgetExpenseId();
        BudgetExpense budgetExpense = new BudgetExpense(budgetExpenseId, new UserName("USER"), firstDateOfMonth(JANUARY, Year.of(2018)),
                ONE, "A_NOTE", "A_TAG");


        Optional<Attachment> actual = s3AttachmentRepository.findAttachmentFor(budgetExpense, new AttachmentFileName("AN_ATTACHMENT_NAME"));
        assertThat(actual, is(empty()));
    }


    @Test
    public void deleteAnAttachment() throws IOException {
        byte[] bytes = new ClassPathResource("schema.sql").getInputStream().readAllBytes();
        BudgetExpenseId budgetExpenseId = BudgetExpenseId.randomBudgetExpenseId();
        AttachmentFileName attachmentFileName = new AttachmentFileName("AN_ATTACHMENT_NAME");
        Attachment attachment = new Attachment(attachmentFileName, CONTENT_TYPE, bytes);
        BudgetExpense budgetExpense = new BudgetExpense(budgetExpenseId,
                new UserName("USER"),
                firstDateOfMonth(JANUARY, Year.of(2018)),
                ONE, "A_NOTE", "A_TAG");
        s3AttachmentRepository.save(budgetExpense, attachment);


        S3AttachmentPathProvider s3AttachmentPathProvider = new S3AttachmentPathProvider(CONTENT_KEY_PREFIX);
        S3Object object = s3client.getObject(BUCKET_NAME, s3AttachmentPathProvider.provide(budgetExpense, attachment.getName()));
        assertNotNull(object);

        log.info(object.toString());

        assertThat(new BudgetExpenseId(object.getObjectMetadata().getUserMetaDataOf(BUDGET_EXPENSE_ID)), is(budgetExpenseId));
        assertThat(object.getObjectMetadata().getUserMetaDataOf(BUDGET_EXPENSE_DATE), is(budgetExpense.getDate().formattedDate()));
        assertThat(object.getObjectMetadata().getUserMetaDataOf(BUDGET_EXPENSE_SEARCH_TAG), is(budgetExpense.getTag()));

        s3AttachmentRepository.delete(budgetExpense, attachmentFileName);

        exception.expect(AmazonS3Exception.class);

        s3client.getObject(BUCKET_NAME, s3AttachmentPathProvider.provide(budgetExpense, attachment.getName()));
    }
}