package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository;


import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.user.UserName;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class JdbcSearchTagRepository implements SearchTagRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public JdbcSearchTagRepository(UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SearchTag findSearchTagBy(String key) {
        return jdbcTemplate.queryForObject("SELECT * FROM SEARCH_TAG WHERE KEY=? AND USER_NAME=?", new Object[]{key, userRepository.currentLoggedUserName()},
                (rs, rowNum) -> new SearchTag(new UserName(rs.getString("USER_NAME")), rs.getString("KEY"), rs.getString("VALUE")));
    }

    @Override
    public List<SearchTag> findAllSearchTag() {
        return jdbcTemplate.query("SELECT * FROM SEARCH_TAG WHERE USER_NAME=?", (rs, rowNum) -> new SearchTag(new UserName(rs.getString("USER_NAME")), rs.getString("KEY"), rs.getString("VALUE")));
    }

    @Override
    public void save(SearchTag searchTag) {
        jdbcTemplate.update("INSERT INTO SEARCH_TAG(KEY, VALUE, USER_NAME) VALUES (?, ?, ?) ON CONFLICT (KEY) DO UPDATE SET VALUE=?", searchTag.getKey(), searchTag.getValue(), searchTag.getValue());
    }

    @Override
    public void delete(String key) {
        jdbcTemplate.update("DELETE FROM SEARCH_TAG WHERE KEY=? AND USER_NAME=?", key, userRepository.currentLoggedUserName());
    }
}
