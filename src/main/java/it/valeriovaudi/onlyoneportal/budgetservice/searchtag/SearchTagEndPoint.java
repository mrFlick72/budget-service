package it.valeriovaudi.onlyoneportal.budgetservice.searchtag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchTagEndPoint {

    private final SearchTagRepository searchTagRepository;

    public SearchTagEndPoint(SearchTagRepository searchTagRepository) {
        this.searchTagRepository = searchTagRepository;
    }

    @GetMapping("/budget-expense/search-tag")
    public ResponseEntity findAllSearchTag() {
        return ResponseEntity.ok(searchTagRepository.findAllSearchTag());
    }

    @PutMapping("/budget-expense/search-tag")
    public ResponseEntity newSearchTag(@RequestBody SearchTag searchTag) {
        searchTagRepository.save(searchTag);
        return ResponseEntity.noContent().build();
    }

}
