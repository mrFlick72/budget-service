package it.valeriovaudi.familybudget.budgetservice.domain.model.budget;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.attachment.AttachmentFileName;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.model.user.UserName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"id"})
public final class BudgetExpense {
    private final BudgetExpenseId id;

    private final UserName userName;
    private final Date date;
    private final Money amount;
    private final String note;
    private final String tag;

    private final List<AttachmentFileName> attachmentFileNames;

    public BudgetExpense(BudgetExpenseId id, UserName userName, Date date, Money amount, String note, String tag) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.tag = tag;

        this.attachmentFileNames = new ArrayList<>();
    }


    public BudgetExpense(BudgetExpenseId id, UserName userName, Date date, Money amount, String note, String tag, List<AttachmentFileName> attachmentFileNames) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.tag = tag;

        this.attachmentFileNames = attachmentFileNames;
    }

    public String attachmentDatePath(){
        LocalDate localDate = date.getLocalDate();
        return join("/", String.valueOf(localDate.getYear()),
                String.valueOf(localDate.getMonth().getValue()),
                String.valueOf(localDate.getDayOfMonth()));
    }
}