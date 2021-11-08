package it.valeriovaudi.onlyoneportal.budgetservice.web.model;


import java.io.Serializable;

public record TotalBySearchTagDetail(String searchTagKey,
                                     String searchTagValue,
                                     String total) implements Serializable {
}
