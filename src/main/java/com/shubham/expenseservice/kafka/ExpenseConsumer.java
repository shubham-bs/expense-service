package com.shubham.expenseservice.kafka;

import com.shubham.expenseservice.dto.ExpenseDto;
import com.shubham.expenseservice.dto.ParsedExpenseDto;
import com.shubham.expenseservice.service.ExpenseService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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
        System.out.println("Merchant: " + event.getMerchant());
        System.out.println("Category: " + event.getCategory());

        ExpenseDto expenseDto = new ExpenseDto();

        expenseDto.setAmount(event.getAmount());
        expenseDto.setMerchant(event.getMerchant());
        expenseDto.setCategory(event.getCategory());
        expenseDto.setDescription(event.getRawMessage());
        expenseDto.setExpenseDate(java.time.LocalDate.now());

        expenseService.createExpenseFromSms(
                event.getUserEmail(),
                expenseDto
        );
    }
}