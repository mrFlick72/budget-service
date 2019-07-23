package it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.processor.DataExporter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@ToString
@EqualsAndHashCode
public final class SpentBudget {

    private final List<BudgetExpense> budgetExpenseList;
    private final List<SearchTag> searchTags;

    public SpentBudget(List<BudgetExpense> budgetExpenseList,
                       List<SearchTag> searchTags) {
        this.budgetExpenseList = budgetExpenseList;
        this.searchTags = searchTags;
    }

    public InputStream printBudgetExpenseList(DataExporter strategy) {
        return strategy.exportData(budgetExpenseList);
    }

    public Money total() {
        return budgetExpenseList.stream()
                .map(BudgetExpense::getAmount)
                .reduce(Money.ZERO, Money::plus);
    }

    public Map<SearchTag, Money> totalForSearchTags() {
        return budgetExpenseList.stream()
                .collect(groupingBy(classifier -> findSearchTagFor(classifier.getTag()),
                        Collectors.mapping(BudgetExpense::getAmount, Collectors.reducing(Money.ZERO, Money::plus))));
    }

    public List<DailyBudgetExpense> dailyBudgetExpenseList() {
        LinkedHashMap<Date, List<BudgetExpense>> result = new LinkedHashMap<>();
        for (BudgetExpense budgetExpense : budgetExpenseList) {
            Date key = budgetExpense.getDate();
            List<BudgetExpense> value = result.getOrDefault(key, new ArrayList<>());
            value.add(budgetExpense);
            result.put(key, value);
        }
        return result.entrySet().stream()
                .map(entry -> new DailyBudgetExpense(entry.getValue(), entry.getKey(),
                        entry.getValue()
                                .stream().map(BudgetExpense::getAmount)
                                .reduce(Money::plus)
                                .orElse(Money.ZERO)))
                .collect(toList());
    }


    private SearchTag findSearchTagFor(String searchTag) {
        return searchTags.stream()
                .filter(search -> search.getKey().equals(searchTag))
                .findFirst()
                .orElse(null);
    }


}
