package com.shubham.expenseservice.kafka;

import com.shubham.expenseservice.dto.ExpenseDto;
import com.shubham.expenseservice.dto.ParsedExpenseDto;
import com.shubham.expenseservice.service.ExpenseService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ExpenseConsumer {

    private final ExpenseService expenseService;

    public ExpenseConsumer(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @KafkaListener(
            topics = "expense.parsed",
            groupId = "expense-group"
    )
    public void consume(ParsedExpenseDto event) {

        System.out.println("Received parsed expense:");
        System.out.println("User: " + event.getUserEmail());
        System.out.println("Amount: " + event.getAmount());
        System.out.println("Currency: " + event.getCurrency());
        System.out.println("Merchant: " + event.getMerchant());
        System.out.println("Category: " + event.getCategory());
        System.out.println("Transaction Type: " + event.getTransactionType());
        System.out.println("Confidence: " + event.getConfidence());

        ExpenseDto expenseDto = new ExpenseDto();

        expenseDto.setAmount(event.getAmount());
        expenseDto.setCurrency(
                event.getCurrency() != null
                        ? event.getCurrency()
                        : "INR"
        );
        expenseDto.setMerchant(event.getMerchant());
        expenseDto.setCategory(event.getCategory());
        expenseDto.setDescription(event.getRawMessage());
        expenseDto.setExpenseDate(resolveExpenseDate(event));

        expenseService.createExpenseFromSms(
                event.getUserEmail(),
                expenseDto
        );
    }

    private LocalDate resolveExpenseDate(ParsedExpenseDto event) {

        String transactionDateTime = event.getTransactionDateTime();

        if (transactionDateTime == null || transactionDateTime.isBlank()) {
            return LocalDate.now();
        }

        try {
            return LocalDateTime
                    .parse(transactionDateTime)
                    .toLocalDate();

        } catch (Exception ignored) {
            return LocalDate.now();
        }
    }
}