package com.shubham.expenseservice.service;

import com.shubham.expenseservice.dto.CategorySummaryDto;
import com.shubham.expenseservice.dto.ExpenseDto;
import com.shubham.expenseservice.dto.MerchantSummaryDto;
import com.shubham.expenseservice.entity.Expense;
import com.shubham.expenseservice.exception.ExpenseNotFoundException;
import com.shubham.expenseservice.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ExpenseDto createExpense(ExpenseDto expenseDto) {

        String email = getCurrentUserEmail();

        Expense expense = toEntity(expenseDto);
        expense.setUserEmail(email);

        Expense saved = expenseRepository.save(expense);

        return toDto(saved);
    }

    @Override
    public ExpenseDto createExpenseFromSms(
            String userEmail,
            ExpenseDto expenseDto
    ) {

        Expense expense = toEntity(expenseDto);

        expense.setUserEmail(userEmail);
        expense.setSource("SMS");

        Expense saved = expenseRepository.save(expense);

        return toDto(saved);
    }

    @Override
    public Page<ExpenseDto> getAllExpenses(
            int page,
            int size,
            String sortBy
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        String email = getCurrentUserEmail();

        return expenseRepository
                .findByUserEmail(email, pageable)
                .map(this::toDto);
    }

    @Override
    public ExpenseDto getExpense(Long id) {

        String email = getCurrentUserEmail();

        Expense expense = expenseRepository
                .findByIdAndUserEmail(id, email)
                .orElseThrow(() ->
                        new ExpenseNotFoundException(id)
                );

        return toDto(expense);
    }

    @Override
    public ExpenseDto updateExpense(
            Long id,
            ExpenseDto expenseDto
    ) {

        String email = getCurrentUserEmail();

        Expense expense = expenseRepository
                .findByIdAndUserEmail(id, email)
                .orElseThrow(() ->
                        new ExpenseNotFoundException(id)
                );

        expense.setAmount(expenseDto.getAmount());
        expense.setCurrency(expenseDto.getCurrency());
        expense.setMerchant(expenseDto.getMerchant());
        expense.setCategory(expenseDto.getCategory());
        expense.setDescription(expenseDto.getDescription());
        expense.setExpenseDate(expenseDto.getExpenseDate());

        Expense updated = expenseRepository.save(expense);

        return toDto(updated);
    }

    @Override
    public void deleteExpense(Long id) {

        String email = getCurrentUserEmail();

        Expense expense = expenseRepository
                .findByIdAndUserEmail(id, email)
                .orElseThrow(() ->
                        new ExpenseNotFoundException(id)
                );

        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseDto> getExpensesByCategory(String category) {

        String email = getCurrentUserEmail();

        return expenseRepository
                .findByCategoryAndUserEmail(category, email)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ExpenseDto> getExpensesBySource(String source) {

        String email = getCurrentUserEmail();

        return expenseRepository
                .findBySourceAndUserEmail(source, email)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ExpenseDto> getExpensesByExpenseDate(
            LocalDate expenseDate
    ) {

        String email = getCurrentUserEmail();

        return expenseRepository
                .findByExpenseDateAndUserEmail(
                        expenseDate,
                        email
                )
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ExpenseDto> getExpensesByDateRange(
            LocalDate startDate,
            LocalDate endDate
    ) {

        String email = getCurrentUserEmail();

        return expenseRepository
                .findByExpenseDateBetweenAndUserEmail(
                        startDate,
                        endDate,
                        email
                )
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ExpenseDto> getExpensesByMerchant(String merchant) {

        String email = getCurrentUserEmail();

        return expenseRepository
                .findByMerchantAndUserEmail(merchant, email)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BigDecimal getTotalSpent() {

        String email = getCurrentUserEmail();

        return expenseRepository.getTotalSpent(email);
    }

    @Override
    public BigDecimal getMonthlySpent(
            int year,
            int month
    ) {

        String email = getCurrentUserEmail();

        YearMonth yearMonth = YearMonth.of(year, month);

        return expenseRepository.getTotalSpentBetween(
                yearMonth.atDay(1),
                yearMonth.atEndOfMonth(),
                email
        );
    }

    @Override
    public List<CategorySummaryDto> getCategorySummary() {

        String email = getCurrentUserEmail();

        return expenseRepository
                .getCategoryWiseTotals(email)
                .stream()
                .map(row -> new CategorySummaryDto(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    @Override
    public List<MerchantSummaryDto> getMerchantSummary() {

        String email = getCurrentUserEmail();

        return expenseRepository
                .getMerchantWiseTotals(email)
                .stream()
                .map(row -> new MerchantSummaryDto(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    @Override
    public BigDecimal getSpentBetween(
            LocalDate startDate,
            LocalDate endDate
    ) {

        String email = getCurrentUserEmail();

        return expenseRepository.getTotalSpentBetween(
                startDate,
                endDate,
                email
        );
    }

    private String getCurrentUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private Expense toEntity(ExpenseDto dto) {

        Expense expense = new Expense();

        expense.setAmount(dto.getAmount());

        expense.setCurrency(
                dto.getCurrency() != null
                        ? dto.getCurrency()
                        : "INR"
        );

        expense.setMerchant(dto.getMerchant());
        expense.setCategory(dto.getCategory());
        expense.setDescription(dto.getDescription());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setSource(dto.getSource());

        return expense;
    }

    private ExpenseDto toDto(Expense expense) {

        ExpenseDto dto = new ExpenseDto();

        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setCurrency(expense.getCurrency());
        dto.setMerchant(expense.getMerchant());
        dto.setCategory(expense.getCategory());
        dto.setDescription(expense.getDescription());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setSource(expense.getSource());

        return dto;
    }
}