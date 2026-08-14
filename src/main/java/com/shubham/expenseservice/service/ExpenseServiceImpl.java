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

        Expense expense = new Expense();
        expense.setAmount(expenseDto.getAmount());
        expense.setCurrency(expenseDto.getCurrency());
        expense.setMerchant(expenseDto.getMerchant());
        expense.setCategory(expenseDto.getCategory());
        expense.setDescription(expenseDto.getDescription());
        expense.setExpenseDate(expenseDto.getExpenseDate());

        Expense saved = expenseRepository.save(expense);
        ExpenseDto response = toDto(saved);
        return response;
    }

    @Override
    public Page<ExpenseDto> getAllExpenses(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return expenseRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Override
    public ExpenseDto getExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        return toDto(expense);
    }

    @Override

    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        expense.setAmount(expenseDto.getAmount());
        expense.setCurrency(expenseDto.getCurrency());
        expense.setMerchant(expenseDto.getMerchant());
        expense.setCategory(expenseDto.getCategory());
        expense.setDescription(expenseDto.getDescription());
        expense.setExpenseDate(expenseDto.getExpenseDate());
        Expense updatedExpense = expenseRepository.save(expense);
        return toDto(updatedExpense);
    }


    public List<ExpenseDto> getExpensesByCategory(String category) {
        return expenseRepository.findByCategory(category)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ExpenseDto> getExpensesBySource(String source) {
        return expenseRepository.findBySource(source)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ExpenseDto> getExpensesByExpenseDate(LocalDate expenseDate) {
        return expenseRepository.findByExpenseDate(expenseDate)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ExpenseDto> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByExpenseDateBetween(startDate, endDate)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void deleteExpense(Long id){

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseDto> getExpensesByMerchant(String merchant) {
        return expenseRepository.findByMerchant(merchant)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BigDecimal getTotalSpent() {
        return expenseRepository.getTotalSpent();
    }

    @Override
    public BigDecimal getMonthlySpent(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return expenseRepository.getTotalSpentBetween(startDate, endDate);
    }

    @Override
    public List<CategorySummaryDto> getCategorySummary() {
        List<Object[]> results = expenseRepository.getCategoryWiseTotals();

        return results.stream()
                .map(row -> new CategorySummaryDto(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public List<MerchantSummaryDto> getMerchantSummary() {
        List<Object[]> results = expenseRepository.getMerchantWiseTotals();

        return results.stream()
                .map(row -> new MerchantSummaryDto(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    @Override
    public BigDecimal getSpentBetween(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getTotalSpentBetween(startDate, endDate);
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

        return dto;
    }
}