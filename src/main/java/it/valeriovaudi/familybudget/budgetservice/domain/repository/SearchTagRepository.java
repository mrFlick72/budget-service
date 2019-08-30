package it.valeriovaudi.familybudget.budgetservice.domain.repository;


import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;

import java.util.List;

public interface SearchTagRepository {

    SearchTag findSearchTagBy(String key);
    List<SearchTag> findAllSearchTag();
    void save(SearchTag searchTag);
    void delete(String key);
}
