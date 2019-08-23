package it.valeriovaudi.familybudget.budgetservice.web.endpoint;

import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.repository.SearchTagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.websocket.server.PathParam;

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

    @DeleteMapping("/budget-expense/search-tag/{key}")
    public ResponseEntity newSearchTag(@PathVariable String key) {
        searchTagRepository.delete(key);
        return ResponseEntity.noContent().build();
    }
}
