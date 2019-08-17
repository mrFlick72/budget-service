package it.valeriovaudi.familybudget.budgetservice.web.endpoint;

import it.valeriovaudi.familybudget.budgetservice.domain.repository.SearchTagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchTagEndPoint {

    private final SearchTagRepository searchTagRepository;

    public SearchTagEndPoint(SearchTagRepository searchTagRepository) {
        this.searchTagRepository = searchTagRepository;
    }

    @GetMapping("/budget-expense/search-tag")
    public ResponseEntity findAllSearchTag(){
        return ResponseEntity.ok(searchTagRepository.findAllSearchTag());
    }
}
