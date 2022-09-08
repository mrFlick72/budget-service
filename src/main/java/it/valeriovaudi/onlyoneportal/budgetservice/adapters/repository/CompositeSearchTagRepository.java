package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;


import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CompositeSearchTagRepository implements SearchTagRepository {

    private final SearchTagRepository primary;
    private final SearchTagRepository secondary;

    public CompositeSearchTagRepository(SearchTagRepository primary, SearchTagRepository secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }


    @Override
    public SearchTag findSearchTagBy(String key) {
        SearchTag searchTagBy = primary.findSearchTagBy(key);
        secondary.findSearchTagBy(key);

        return searchTagBy;
    }

    @Override
    public List<SearchTag> findAllSearchTag() {
        List<SearchTag> searchTagBy = primary.findAllSearchTag();
        secondary.findAllSearchTag();

        return searchTagBy;
    }

    @Override
    public void save(SearchTag searchTag) {
        primary.save(searchTag);
        secondary.save(searchTag);
    }

    @Override
    public void delete(String key) {
        primary.delete(key);
        secondary.delete(key);
    }
}
