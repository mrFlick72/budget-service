package it.valeriovaudi.familybudget.budgetservice.adapters.repository.attachment;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.Attachment;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentUploadException;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.AttachmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
public class RestAttachmentRepository implements AttachmentRepository {

    private static final String BUDGET_EXPENSE_ID = "budget-expense-id";
    private static final String BUDGET_EXPENSE_DATE = "budget-expense-date";
    private static final String BUDGET_EXPENSE_SEARCH_TAG = "budget-expense-search-tag";

    AmazonS3 s3client;
    S3AttachmentPathProvider s3AttachmentPathProvider;
    String bucketName;

    private final String uri;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RestAttachmentRepository(String baseUri,
                                    RestTemplate restTemplate,
                                    ObjectMapper objectMapper) {
        this.uri = baseUri;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(BudgetExpense budgetExpense, Attachment attachment) {
        try {
            restTemplate.exchange(uri, HttpMethod.PUT, saveAttachmentEntity(budgetExpense, attachment), Void.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new AttachmentUploadException(e.getMessage(), e);
        }
    }

    public HttpEntity saveAttachmentEntity(BudgetExpense budgetExpense, Attachment attachment) throws IOException {
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.MULTIPART_FORM_DATA_VALUE));

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileSystemResourceFor(attachment));
        body.add("path", budgetExpense.attachmentDatePath());
        body.add("metadata", objectMapper.writeValueAsBytes(metadataFor(budgetExpense)));

        HttpEntity httpEntity = new HttpEntity(body, header);
        return httpEntity;
    }

    private HttpEntity fileSystemResourceFor(Attachment attachment) throws IOException {
        byte[] bytes = attachment.getContent();
        MultiValueMap<String, String> fileMetadata = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(attachment.getName().getFileName())
                .build();
        fileMetadata.add(HttpHeaders.CONTENT_TYPE, attachment.getContentType());
        fileMetadata.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(attachment.getContent().length));
        fileMetadata.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        return new HttpEntity<>(bytes, fileMetadata);
    }


    private Map<String, String> metadataFor(BudgetExpense budgetExpense) {
        return Map.of(
                BUDGET_EXPENSE_ID, budgetExpense.getId().getContent(),
                BUDGET_EXPENSE_DATE, budgetExpense.getDate().formattedDate(),
                BUDGET_EXPENSE_SEARCH_TAG, budgetExpense.getTag()
        );
    }


    private String objectKeyFor(BudgetExpense budgetExpense, AttachmentFileName attachment) {
        return s3AttachmentPathProvider.provide(budgetExpense, attachment);
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
        String findAttachmentUri = attachmentUriFor(budgetExpense, attachmentFileName);
        return tryToGetAttachmentFor(findAttachmentUri)
                .map(response -> new Attachment(attachmentFileName,
                        response.getHeaders().getContentType().toString(),
                        response.getBody()));
    }

    private Optional<ResponseEntity<byte[]>> tryToGetAttachmentFor(String findAttachmentUri) {
        try {
            return Optional.of(restTemplate.exchange(findAttachmentUri, HttpMethod.GET, HttpEntity.EMPTY, byte[].class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String attachmentUriFor(BudgetExpense budgetExpense, AttachmentFileName attachmentFileName) {
        String findAttachmentUri = UriComponentsBuilder.fromUriString(uri)
                .queryParam("path", budgetExpense.attachmentDatePath())
                .queryParam("fileName", FilenameUtils.getBaseName(attachmentFileName.getFileName()))
                .queryParam("fileExt", FilenameUtils.getExtension(attachmentFileName.getFileName()))
                .build().toUriString();
        return findAttachmentUri;
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

    private String getAttachmentFileNameFor(String s3Key) {
        String[] split = s3Key.split("/");
        return split[split.length - 1];
    }

}
