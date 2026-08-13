package com.shubham.expenseservice.entity;
import jakarta.persistence.*;
        import lombok.*;

        import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    private BigDecimal amount;

    private String currency;

    private String merchant;

    private String category;

    private String description;

    private LocalDate expenseDate;

    private String source;

    private String rawMessageId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
