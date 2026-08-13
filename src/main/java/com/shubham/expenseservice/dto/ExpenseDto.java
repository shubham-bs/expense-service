package com.shubham.expenseservice.dto;

import lombok.*;

        import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDto {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private String merchant;
    private String category;
    private String description;
    private LocalDate expenseDate;
}
