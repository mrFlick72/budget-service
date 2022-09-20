package it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb;

import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;

public interface BudgetDynamoDbIdFactory<ID, OUT> {

    ID budgetIdFrom(OUT budget);

    String partitionKeyFrom(Date date, UserName userName);

    String partitionKeyFrom(ID id);

    String rangeKeyFrom(ID id);


}