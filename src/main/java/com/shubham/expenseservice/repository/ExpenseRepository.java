package com.shubham.expenseservice.repository;

import com.shubham.expenseservice.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndUserEmail(Long id, String userEmail);

    Page<Expense> findByUserEmail(String userEmail, Pageable pageable);

    List<Expense> findByCategoryAndUserEmail(String category, String userEmail);

    List<Expense> findBySourceAndUserEmail(String source, String userEmail);

    List<Expense> findByExpenseDateAndUserEmail(LocalDate expenseDate, String userEmail);

    List<Expense> findByExpenseDateBetweenAndUserEmail(
            LocalDate startDate,
            LocalDate endDate,
            String userEmail
    );

    List<Expense> findByMerchantAndUserEmail(String merchant, String userEmail);

    @Query("select coalesce(sum(e.amount), 0) from Expense e where e.userEmail = :userEmail")
    BigDecimal getTotalSpent(@Param("userEmail") String userEmail);

    @Query("""
           select coalesce(sum(e.amount), 0)
           from Expense e
           where e.expenseDate between :startDate and :endDate and e.userEmail = :userEmail
           """)
    BigDecimal getTotalSpentBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                    @Param("userEmail") String userEmail);

    @Query("""
           select e.category, coalesce(sum(e.amount), 0)
           from Expense e
           where e.userEmail = :userEmail
           group by e.category
           """)
    List<Object[]> getCategoryWiseTotals(@Param("userEmail") String userEmail);
    @Query("""
       select e.merchant, coalesce(sum(e.amount), 0)
           from Expense e
           where e.userEmail = :userEmail
           group by e.merchant
           """)
    List<Object[]> getMerchantWiseTotals(@Param("userEmail") String userEmail);
}
