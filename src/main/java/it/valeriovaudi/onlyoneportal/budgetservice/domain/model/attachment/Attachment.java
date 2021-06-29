package it.valeriovaudi.onlyoneportal.budgetservice.domain.model.attachment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Attachment {

    private final AttachmentFileName name;
    private final String contentType;
    private final byte[] content;

    public Attachment(AttachmentFileName name,
                      String contentType,
                      byte[] content) {
        this.name = name;
        this.contentType = contentType;
        this.content = content;
    }

    public static Attachment emptyAttachment() {
        return new Attachment(AttachmentFileName.emptyFileName(), "", new byte[0]);
    }

}