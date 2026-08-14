package com.shubham.expenseservice.repository;

import com.shubham.expenseservice.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByCategory(String category);
    List<Expense> findBySource(String source);
    List<Expense> findByExpenseDate(LocalDate expenseDate);
    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expense> findByMerchant(String merchant);

    @Query("select coalesce(sum(e.amount), 0) from Expense e")
    BigDecimal getTotalSpent();

    @Query("""
           select coalesce(sum(e.amount), 0)
           from Expense e
           where e.expenseDate between :startDate and :endDate
           """)
    BigDecimal getTotalSpentBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("""
           select e.category, coalesce(sum(e.amount), 0)
           from Expense e
           group by e.category
           """)
    List<Object[]> getCategoryWiseTotals();

    @Query("""
       select e.merchant, coalesce(sum(e.amount), 0)
       from Expense e
       group by e.merchant
       """)
    List<Object[]> getMerchantWiseTotals();
}
