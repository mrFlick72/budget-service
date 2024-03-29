package it.valeriovaudi.onlyoneportal.budgetservice.budget.expense.model;

import it.valeriovaudi.onlyoneportal.budgetservice.budget.Money;
import it.valeriovaudi.onlyoneportal.budgetservice.searchtag.SearchTag;
import it.valeriovaudi.onlyoneportal.budgetservice.time.Date;
import it.valeriovaudi.onlyoneportal.budgetservice.user.UserName;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public final class SpentBudget {

    private final List<BudgetExpense> budgetExpenseList;
    private final Map<String, String> searchTags;

    public SpentBudget(List<BudgetExpense> budgetExpenseList,
                       List<SearchTag> searchTags) {
        this.budgetExpenseList = budgetExpenseList;
        this.searchTags = adaptSearchTag(searchTags);
    }

    public Money total() {
        return budgetExpenseList.stream()
                .map(BudgetExpense::amount)
                .reduce(Money.ZERO, Money::plus);
    }

    public Map<SearchTag, Money> totalForSearchTags() {
        return budgetExpenseList.stream()
                .collect(groupingBy(classifier -> findSearchTagFor(classifier.userName(), classifier.tag()),
                        Collectors.mapping(BudgetExpense::amount, Collectors.reducing(Money.ZERO, Money::plus))));
    }

    public List<DailyBudgetExpense> dailyBudgetExpenseList() {
        LinkedHashMap<Date, List<BudgetExpense>> result = new LinkedHashMap<>();
        for (BudgetExpense budgetExpense : budgetExpenseList) {
            Date key = budgetExpense.date();
            List<BudgetExpense> value = result.getOrDefault(key, new ArrayList<>());
            value.add(budgetExpense);
            result.put(key, value);
        }
        return result.entrySet().stream()
                .map(entry -> new DailyBudgetExpense(entry.getValue(), entry.getKey(),
                        entry.getValue()
                                .stream().map(BudgetExpense::amount)
                                .reduce(Money::plus)
                                .orElse(Money.ZERO)))
                .collect(toList());
    }


    private SearchTag findSearchTagFor(UserName userName, String searchTag) {
        return Optional.ofNullable(this.searchTags.get(searchTag))
                .map(searchTagValue -> new SearchTag(searchTag, searchTagValue))
                .orElse(null);
    }

    private Map adaptSearchTag(List<SearchTag> searchTags) {
        return Optional.ofNullable(searchTags)
                .map(tags ->
                        tags.stream()
                                .map(search -> {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put(search.key(), search.value());
                                    return map;
                                })
                                .reduce(new HashMap<>(), (m1, m2) -> {
                                    m1.putAll(m2);
                                    return m1;
                                })
                ).orElse(new HashMap<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpentBudget that = (SpentBudget) o;
        return Objects.equals(budgetExpenseList, that.budgetExpenseList) && Objects.equals(searchTags, that.searchTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budgetExpenseList, searchTags);
    }
}