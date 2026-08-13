package com.shubham.expenseservice.controller;


import com.shubham.expenseservice.dto.ExpenseDto;
import com.shubham.expenseservice.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseDto createExpense(@RequestBody ExpenseDto expenseDto) {
        return expenseService.createExpense(expenseDto);
    }
    @GetMapping("/{id}")
    public ExpenseDto getExpense(@PathVariable Long id) {
        return expenseService.getExpense(id);
    }
}
