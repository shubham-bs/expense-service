package com.shubham.expenseservice.controller;

import com.shubham.expenseservice.dto.CategorySummaryDto;
import com.shubham.expenseservice.dto.ExpenseDto;
import com.shubham.expenseservice.dto.MerchantSummaryDto;
import com.shubham.expenseservice.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(
            @Valid @RequestBody ExpenseDto expenseDto) {

        ExpenseDto created = expenseService.createExpense(expenseDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDto> getExpense(@PathVariable Long id) {

        return ResponseEntity.ok(
                expenseService.getExpense(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseDto expenseDto) {

        return ResponseEntity.ok(
                expenseService.updateExpense(id, expenseDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {

        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter/category")
    public List<ExpenseDto> getByCategory(
            @RequestParam String category) {

        return expenseService.getExpensesByCategory(category);
    }

    @GetMapping("/filter/source")
    public List<ExpenseDto> getBySource(
            @RequestParam String source) {

        return expenseService.getExpensesBySource(source);
    }

    @GetMapping("/filter/date")
    public List<ExpenseDto> getByDate(
            @RequestParam LocalDate expenseDate) {

        return expenseService.getExpensesByExpenseDate(expenseDate);
    }

    @GetMapping("/filter/range")
    public List<ExpenseDto> getByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return expenseService.getExpensesByDateRange(startDate, endDate);
    }

    @GetMapping("/filter/merchant")
    public List<ExpenseDto> getByMerchant(
            @RequestParam String merchant) {

        return expenseService.getExpensesByMerchant(merchant);
    }

    @GetMapping
    public Page<ExpenseDto> getAllExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        return expenseService.getAllExpenses(page, size, sortBy);
    }

    @GetMapping("/summary/total")
    public BigDecimal getTotalSpent() {
        return expenseService.getTotalSpent();
    }

    @GetMapping("/summary/monthly")
    public BigDecimal getMonthlySpent(
            @RequestParam int year,
            @RequestParam int month) {

        return expenseService.getMonthlySpent(year, month);
    }

    @GetMapping("/summary/category")
    public List<CategorySummaryDto> getCategorySummary() {
        return expenseService.getCategorySummary();
    }

    @GetMapping("/summary/merchant")
    public List<MerchantSummaryDto> getMerchantSummary() {
        return expenseService.getMerchantSummary();
    }

    @GetMapping("/summary/range")
    public BigDecimal getSpentBetween(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return expenseService.getSpentBetween(startDate, endDate);
    }
}