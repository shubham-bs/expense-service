package com.shubham.expenseservice.repository;

import com.shubham.expenseservice.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
