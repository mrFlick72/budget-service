package it.valeriovaudi.onlyoneportal.budgetservice.searchtag;


import java.util.List;

public interface SearchTagRepository {

    SearchTag findSearchTagBy(String key);
    List<SearchTag> findAllSearchTag();
    void save(SearchTag searchTag);
}
