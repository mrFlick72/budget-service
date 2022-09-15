package it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint.BudgetSearchCriteriaRepresentation;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Year;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/budget/revenue")
public class BudgetRevenueEndPoint {

    private final BudgetRevenueRepository budgetRevenueRepository;
    private final FindBudgetRevenue findBudgetRevenue;

    private final BudgetRevenueAdapter budgetRevenueAdapter;
    private final UserRepository userRepository;

    public BudgetRevenueEndPoint(BudgetRevenueRepository budgetRevenueRepository,
                                 FindBudgetRevenue findBudgetRevenue, BudgetRevenueAdapter budgetRevenueAdapter,
                                 UserRepository userRepository) {
        this.budgetRevenueRepository = budgetRevenueRepository;
        this.findBudgetRevenue = findBudgetRevenue;
        this.budgetRevenueAdapter = budgetRevenueAdapter;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity newBudgetRevenue(@RequestBody BudgetRevenueRepresentation budgetRevenueRepresentation) {
        budgetRevenueRepository.save(new BudgetRevenue(null, userRepository.currentLoggedUserName().content(),
                Date.dateFor(budgetRevenueRepresentation.date()),
                Money.moneyFor(budgetRevenueRepresentation.amount()),
                budgetRevenueRepresentation.note()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBudgetRevenue(@PathVariable("id") String id,
                                              @RequestBody BudgetRevenueRepresentation representation) {
        BudgetRevenue budgetRevenue = budgetRevenueAdapter.fromRepresentationToModel(new BudgetRevenueRepresentation(
                id,
                representation.date(),
                representation.amount(),
                representation.note()
        ));
        budgetRevenueRepository.update(budgetRevenue);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteBudgetRevenue(@PathVariable("id") String id) {
        budgetRevenueRepository.delete(new BudgetRevenueId(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity findAllBudgetRevenue(@RequestParam("q") BudgetSearchCriteriaRepresentation searchCriteria) {
        Year year = Year.of(searchCriteria.year());

        return ResponseEntity.ok(findBudgetRevenue.findBy(year)
                .stream().map(budgetRevenueAdapter::fromDomainToRepresentation)
                .collect(toList()));
    }

}