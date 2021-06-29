package it.valeriovaudi.onlyoneportal.budgetservice.domain.model.attachment;


public class AttachmentUploadException extends RuntimeException {
    private final String message;
    private final Exception e;

    public AttachmentUploadException(String message, Exception e) {
        this.message = message;
        this.e = e;
    }
}
