package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;

import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;

import java.util.List;

public class DynamoDBSearchTagRepository implements SearchTagRepository {
    @Override
    public SearchTag findSearchTagBy(String key) {
        return null;
    }

    @Override
    public List<SearchTag> findAllSearchTag() {
        return null;
    }

    @Override
    public void save(SearchTag searchTag) {

    }

    @Override
    public void delete(String key) {

    }
}
