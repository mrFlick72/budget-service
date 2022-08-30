package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;


import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CompositeSearchTagRepository implements SearchTagRepository {

    private final SearchTagRepository database;
    private final SearchTagRepository dynamodb;

    public CompositeSearchTagRepository(SearchTagRepository database, SearchTagRepository dynamodb) {
        this.database = database;
        this.dynamodb = dynamodb;
    }


    @Override
    public SearchTag findSearchTagBy(String key) {
        SearchTag searchTagBy = database.findSearchTagBy(key);
        dynamodb.findSearchTagBy(key);

        return searchTagBy;
    }

    @Override
    public List<SearchTag> findAllSearchTag() {
        List<SearchTag> searchTagBy = database.findAllSearchTag();
        dynamodb.findAllSearchTag();

        return searchTagBy;
    }

    @Override
    public void save(SearchTag searchTag) {
        database.save(searchTag);
        dynamodb.save(searchTag);
    }

    @Override
    public void delete(String key) {
        database.delete(key);
        dynamodb.delete(key);
    }
}
