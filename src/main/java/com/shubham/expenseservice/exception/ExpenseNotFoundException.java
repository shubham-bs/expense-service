package com.shubham.expenseservice.exception;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(Long id) {

        super("Expense with id " + id + " not found");

    }

}