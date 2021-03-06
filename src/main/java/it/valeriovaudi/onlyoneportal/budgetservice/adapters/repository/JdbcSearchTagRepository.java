package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;


import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class JdbcSearchTagRepository implements SearchTagRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSearchTagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SearchTag findSearchTagBy(String key) {
        return jdbcTemplate.queryForObject("SELECT * FROM SEARCH_TAG WHERE KEY=?", new Object[]{key},
                (rs, rowNum) -> new SearchTag(rs.getString("KEY"), rs.getString("VALUE")));
    }

    @Override
    public List<SearchTag> findAllSearchTag() {
        return jdbcTemplate.query("SELECT * FROM SEARCH_TAG", (rs, rowNum) -> new SearchTag(rs.getString("KEY"), rs.getString("VALUE")));
    }

    @Override
    public void save(SearchTag searchTag) {
        jdbcTemplate.update("INSERT INTO SEARCH_TAG(KEY, VALUE) VALUES (?, ?) ON CONFLICT (KEY) DO UPDATE SET VALUE=?", searchTag.getKey(), searchTag.getValue(), searchTag.getValue());
    }

    @Override
    public void delete(String key) {
        jdbcTemplate.update("DELETE FROM SEARCH_TAG WHERE KEY=?", key);
    }
}
