package it.valeriovaudi.familybudget.budgetservice.web.endpoint;


import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseId;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.BudgetExpenseNotFoundException;
import it.valeriovaudi.familybudget.budgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.CreateBudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.DeleteBudgetExpense;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.FindSpentBudget;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.UpdateBudgetExpenseDetails;
import it.valeriovaudi.familybudget.budgetservice.web.adapter.BudgetExpenseAdapter;
import it.valeriovaudi.familybudget.budgetservice.web.adapter.SpentBudgetAdapter;
import it.valeriovaudi.familybudget.budgetservice.web.model.BudgetExpenseRepresentation;
import it.valeriovaudi.familybudget.budgetservice.web.model.BudgetSearchCriteriaRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Slf4j
@RestController
@RequestMapping("/budget/expense")
public class BudgetExpenseEndPoint {

    private final FindSpentBudget findSpentBudget;
    private final SpentBudgetAdapter spentBudgetAdapter;
    private final BudgetExpenseAdapter budgetExpenseAdapter;
    private final UpdateBudgetExpenseDetails updateBudgetExpenseDetails;
    private final DeleteBudgetExpense deleteBudgetExpense;
    private final CreateBudgetExpense createBudgetExpense;


    public BudgetExpenseEndPoint(FindSpentBudget findSpentBudget,
                                 SpentBudgetAdapter spentBudgetAdapter,
                                 BudgetExpenseAdapter budgetExpenseAdapter,
                                 UpdateBudgetExpenseDetails updateBudgetExpenseDetails,
                                 DeleteBudgetExpense deleteBudgetExpense,
                                 CreateBudgetExpense createBudgetExpense) {
        this.findSpentBudget = findSpentBudget;
        this.spentBudgetAdapter = spentBudgetAdapter;
        this.budgetExpenseAdapter = budgetExpenseAdapter;
        this.updateBudgetExpenseDetails = updateBudgetExpenseDetails;
        this.deleteBudgetExpense = deleteBudgetExpense;
        this.createBudgetExpense = createBudgetExpense;
    }

    @GetMapping
    public ResponseEntity getBudgetExpenseList(@RequestParam("q") BudgetSearchCriteriaRepresentation budgetExpenseRequest) {
        final SpentBudget findSpentBudgetBy = findSpentBudget.findBy(Month.of(budgetExpenseRequest.getMonth()),
                Year.of(budgetExpenseRequest.getYear()), budgetExpenseRequest.getSearchTagList());

        return ResponseEntity.ok(spentBudgetAdapter.domainToRepresentationModel(findSpentBudgetBy));
    }

    // todo endpoint temporaneo
    @GetMapping("/year/{year}")
    public ResponseEntity getYearlyBudgetExpenseList(@PathVariable("year") Integer year) {
        Year domainYear = Year.of(year);

        List<HashMap> aggregate =
                Stream.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH,
                        Month.APRIL, Month.MAY, Month.JUNE,
                        Month.JULY, Month.AUGUST, Month.SEPTEMBER,
                        Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER)
                        .map(month -> asList(month, findSpentBudget.findBy(month, domainYear, new ArrayList<>())))
                        .map(spentBudget -> {
                            HashMap hashMap = new HashMap();
                            hashMap.put("x", ((Month) spentBudget.get(0)).monthValue());
                            hashMap.put("y", ((SpentBudget) spentBudget.get(1)).total().stringifyAmount());
                            return hashMap;
                        })
                        .collect(Collectors.toList());


        return ResponseEntity.ok(aggregate);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBudgetExpense(@PathVariable("id") String id, @RequestBody BudgetExpenseRepresentation request) {
        request.setId(id);
        updateBudgetExpenseDetails.updateWithoutAttachment(budgetExpenseAdapter.representationModelToDomainModel(request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity newBudgetExpense(@RequestBody BudgetExpenseRepresentation budgetExpenseRepresentation) {
        createBudgetExpense.newBudgetExpense(budgetExpenseAdapter.newBudgetExpenseRequestFromRepresentation(budgetExpenseRepresentation));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBudgetExpense(@PathVariable("id") String id) {
        deleteBudgetExpense.delete(new BudgetExpenseId(id));
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BudgetExpenseNotFoundException.class)
    public ResponseEntity handleBudgetExpenseNotFoundException(BudgetExpenseNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}