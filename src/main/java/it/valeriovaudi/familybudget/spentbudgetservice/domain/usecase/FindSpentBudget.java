package it.valeriovaudi.familybudget.spentbudgetservice.domain.usecase;

import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.SearchTag;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.BudgetExpense;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.budget.SpentBudget;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Date;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.model.user.UserName;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.BudgetExpenseRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.SearchTagRepository;
import it.valeriovaudi.familybudget.spentbudgetservice.domain.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class FindSpentBudget {

    private final UserRepository userRepository;
    private final BudgetExpenseRepository budgetExpenseRepository;
    private final SearchTagRepository searchTagRepository;

    public FindSpentBudget(UserRepository userRepository,
                           BudgetExpenseRepository budgetExpenseRepository,
                           SearchTagRepository searchTagRepository) {
        this.userRepository = userRepository;
        this.budgetExpenseRepository = budgetExpenseRepository;
        this.searchTagRepository = searchTagRepository;
    }

    public SpentBudget findBy(Month month, Year year, List<String> searchTagList) {
        UserName userName = userRepository.currentLoggedUserName();
        String[] searchTags = searchTags(searchTagList);

        List<BudgetExpense> budgetExpenseList =
                budgetExpenseRepository.findByDateRange(userName, Date.firstDateOfMonth(month, year),
                        Date.lastDateOfMonth(month, year),
                        searchTags);

        List<SearchTag> allSearchTagFor = getAllSearchTagFor(budgetExpenseList);

        return new SpentBudget(orderByDate(budgetExpenseList), allSearchTagFor);
    }

    private String[] searchTags(List<String> searchTagList) {
        return Optional.ofNullable(searchTagList)
                .map(searchTagListAux -> searchTagListAux.stream()
                        .filter(tag -> !"".equals(tag.trim()))
                        .collect(toList()))
                .map(tags -> tags.toArray(new String[tags.size()]))
                .orElse(new String[0]);
    }

    private List<BudgetExpense> orderByDate(List<BudgetExpense> budgetExpenseListFromRepository) {
        return budgetExpenseListFromRepository.stream()
                .sorted(Comparator.comparing(BudgetExpense::getDate))
                .collect(toList());
    }

    private List<SearchTag> getAllSearchTagFor(List<BudgetExpense> budgetExpenses) {
        return budgetExpenses.stream().map(BudgetExpense::getTag)
                .distinct().map(searchTagRepository::findSearchTagBy)
                .collect(toList());
    }
}