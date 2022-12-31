package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint;


import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.CreateBudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.DeleteBudgetExpense;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.FindSpentBudget;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.action.UpdateBudgetExpenseDetails;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter.BudgetExpenseConverter;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.converter.SpentBudgetConverter;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseId;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.BudgetExpenseNotFoundException;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model.SpentBudget;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Month;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Year;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budget/expense")
public class BudgetExpenseEndPoint {

    private final FindSpentBudget findSpentBudget;
    private final SpentBudgetConverter spentBudgetConverter;
    private final BudgetExpenseConverter budgetExpenseConverter;
    private final UpdateBudgetExpenseDetails updateBudgetExpenseDetails;
    private final DeleteBudgetExpense deleteBudgetExpense;
    private final CreateBudgetExpense createBudgetExpense;


    public BudgetExpenseEndPoint(FindSpentBudget findSpentBudget,
                                 SpentBudgetConverter spentBudgetConverter,
                                 BudgetExpenseConverter budgetExpenseConverter,
                                 UpdateBudgetExpenseDetails updateBudgetExpenseDetails,
                                 DeleteBudgetExpense deleteBudgetExpense,
                                 CreateBudgetExpense createBudgetExpense) {
        this.findSpentBudget = findSpentBudget;
        this.spentBudgetConverter = spentBudgetConverter;
        this.budgetExpenseConverter = budgetExpenseConverter;
        this.updateBudgetExpenseDetails = updateBudgetExpenseDetails;
        this.deleteBudgetExpense = deleteBudgetExpense;
        this.createBudgetExpense = createBudgetExpense;
    }

    @GetMapping
    public ResponseEntity getBudgetExpenseList(@RequestParam("q") BudgetSearchCriteriaRepresentation budgetExpenseRequest) {
        final SpentBudget findSpentBudgetBy = findSpentBudget.findBy(Month.of(budgetExpenseRequest.month()),
                Year.of(budgetExpenseRequest.year()), budgetExpenseRequest.searchTagList());

        return ResponseEntity.ok(spentBudgetConverter.domainToRepresentationModel(findSpentBudgetBy));
    }

    @PutMapping
    public ResponseEntity getBudgetExpenseListBy(@RequestBody BudgetSearchCriteriaRepresentation budgetExpenseRequest) {
        SpentBudget findSpentBudgetBy = findSpentBudget.findBy(Month.of(budgetExpenseRequest.month()),
                Year.of(budgetExpenseRequest.year()), budgetExpenseRequest.searchTagList());

        return ResponseEntity.ok(spentBudgetConverter.domainToRepresentationModel(findSpentBudgetBy));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBudgetExpense(@PathVariable("id") String id, @RequestBody BudgetExpenseRepresentation request) {
        updateBudgetExpenseDetails.updateWithoutAttachment(budgetExpenseConverter.representationModelToDomainModel(
                new BudgetExpenseRepresentation(
                        id,
                        request.date(),
                        request.amount(),
                        request.note(),
                        request.tagKey(),
                        request.tagValue()
                )
        ));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity newBudgetExpense(@RequestBody BudgetExpenseRepresentation budgetExpenseRepresentation) {
        createBudgetExpense.newBudgetExpense(budgetExpenseConverter.newBudgetExpenseRequestFromRepresentation(budgetExpenseRepresentation));
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