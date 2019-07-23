package it.valeriovaudi.familybudget.spentbudgetservice.web.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
public class TotalBySearchTagDetail implements Serializable {

    private String searchTagKey;
    private String searchTagValue;
    private String total;

    public TotalBySearchTagDetail() {
    }

    public TotalBySearchTagDetail(String searchTagKey, String searchTagValue, String total) {
        this.searchTagKey = searchTagKey;
        this.searchTagValue = searchTagValue;
        this.total = total;
    }
}
