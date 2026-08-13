package com.shubham.expenseservice.service;

import com.shubham.expenseservice.dto.ExpenseDto;

import java.util.List;

public interface ExpenseService {
    ExpenseDto createExpense(ExpenseDto expenseDto);
    List<ExpenseDto> getAllExpenses();
    ExpenseDto getExpense(Long id);
    ExpenseDto updateExpense(Long id, ExpenseDto expenseDto);
    void deleteExpense(Long id);
}
