package it.valeriovaudi.onlyoneportal.budgetservice.web.endpoint;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.endpoint.BudgetSearchCriteriaRepresentation;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueId;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.BudgetRevenueRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.budget.revenue.FindBudgetRevenue;
import it.valeriovaudi.onlyoneportal.budgetservice.domain.model.IdProvider;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Year;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserRepository;
import it.valeriovaudi.onlyoneportal.budgetservice.web.adapter.BudgetRevenueAdapter;
import it.valeriovaudi.onlyoneportal.budgetservice.web.model.BudgetRevenueRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("/budget/revenue")
public class BudgetRevenueEndPoint {

    private final BudgetRevenueRepository budgetRevenueRepository;
    private final FindBudgetRevenue findBudgetRevenue;

    private final BudgetRevenueAdapter budgetRevenueAdapter;
    private final IdProvider idProvider;
    private final UserRepository userRepository;

    public BudgetRevenueEndPoint(BudgetRevenueRepository budgetRevenueRepository,
                                 FindBudgetRevenue findBudgetRevenue, BudgetRevenueAdapter budgetRevenueAdapter,
                                 IdProvider idProvider, UserRepository userRepository) {
        this.budgetRevenueRepository = budgetRevenueRepository;
        this.findBudgetRevenue = findBudgetRevenue;
        this.budgetRevenueAdapter = budgetRevenueAdapter;
        this.idProvider = idProvider;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity newBudgetRevenue(@RequestBody BudgetRevenueRepresentation budgetRevenueRepresentation) {
        budgetRevenueRepository.save(new BudgetRevenue(idProvider.id(), new BudgetRevenueId(idProvider.id()), userRepository.currentLoggedUserName().getContent(),
                Date.dateFor(budgetRevenueRepresentation.getDate()),
                Money.moneyFor(budgetRevenueRepresentation.getAmount()),
                budgetRevenueRepresentation.getNote()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBudgetRevenue(@PathVariable("id") String id,
                                              @RequestBody BudgetRevenueRepresentation budgetRevenueRepresentation) {
        budgetRevenueRepresentation.setId(id);
        BudgetRevenue budgetRevenue = budgetRevenueAdapter.fromRepresentationToModel(budgetRevenueRepresentation);
        budgetRevenueRepository.update(budgetRevenue);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteBudgetRevenue(@PathVariable("id") String id) {
        budgetRevenueRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity findAllBudgetRevenue(@RequestParam("q") BudgetSearchCriteriaRepresentation searchCriteria) {
        Year year = Year.of(searchCriteria.getYear());

        return ResponseEntity.ok(findBudgetRevenue.findBy(year)
                .stream().map(budgetRevenueAdapter::fromDomainToRepresentation)
                .collect(toList()));
    }

}
