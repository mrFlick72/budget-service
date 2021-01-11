package it.valeriovaudi.familybudget.budgetservice.domain.model.budget;

import it.valeriovaudi.familybudget.budgetservice.domain.model.Money;
import it.valeriovaudi.familybudget.budgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.budgetservice.domain.processor.DataExporter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@ToString
@EqualsAndHashCode
public final class SpentBudget {

    private final List<BudgetExpense> budgetExpenseList;
    private final Map<String, String> searchTags;

    public SpentBudget(List<BudgetExpense> budgetExpenseList,
                       List<SearchTag> searchTags) {
        this.budgetExpenseList = budgetExpenseList;
        this.searchTags = adaptSearchTag(searchTags);
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
        return Optional.ofNullable(searchTags.get(searchTag))
                .map(searchTagValue -> new SearchTag(searchTag, searchTagValue))
                .orElse(null);
    }

    private Map adaptSearchTag(List<SearchTag> searchTags) {
        return searchTags.stream()
                .map(search -> Map.of(search.getKey(), search.getValue()))
                .reduce(new HashMap<>(), (m1, m2) -> {
                    m1.putAll(m2);
                    return m2;
                });
    }

}