package com.shubham.expenseservice.service;

import com.shubham.expenseservice.dto.ExpenseDto;
import com.shubham.expenseservice.entity.Expense;
import com.shubham.expenseservice.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

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
        ExpenseDto response = new ExpenseDto();
        response.setId(saved.getId());
        response.setAmount(saved.getAmount());
        response.setCurrency(saved.getCurrency());
        response.setMerchant(saved.getMerchant());
        response.setCategory(saved.getCategory());
        response.setDescription(saved.getDescription());
        response.setExpenseDate(saved.getExpenseDate());
        return response;
    }

    @Override
    public List<ExpenseDto> getAllExpenses() {
        return List.of();
    }

    @Override
    public ExpenseDto getExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        return toDto(expense);
    }

    @Override
    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {
        return null;
    }

    @Override
    public void deleteExpense(Long id) {

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
