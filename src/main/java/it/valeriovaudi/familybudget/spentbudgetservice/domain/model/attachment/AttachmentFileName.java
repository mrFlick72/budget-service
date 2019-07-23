package it.valeriovaudi.familybudget.spentbudgetservice.domain.model.attachment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class AttachmentFileName {
    private final String fileName;

    public AttachmentFileName(String fileName) {
        this.fileName = fileName;
    }

    public static AttachmentFileName emptyFileName(){
        return new AttachmentFileName("");
    }
}
