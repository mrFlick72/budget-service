package it.valeriovaudi.onlyoneportal.budgetservice.searchtag;

import it.valeriovaudi.onlyoneportal.budgetservice.infrastructure.dynamodb.SaltGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchTagEndPoint {

    private final SearchTagRepository searchTagRepository;
    private final SaltGenerator saltGenerator;

    public SearchTagEndPoint(SearchTagRepository searchTagRepository, SaltGenerator saltGenerator) {
        this.searchTagRepository = searchTagRepository;
        this.saltGenerator = saltGenerator;
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

    @PutMapping("/budget-expense/search-tag/v2")
    public ResponseEntity newSearchTagV2(@RequestBody V2SearchTagRequest searchTag) {
        searchTagRepository.save(new SearchTag(saltGenerator.newSalt(), searchTag.value()));
        return ResponseEntity.noContent().build();
    }

}

record V2SearchTagRequest(String value) {
}