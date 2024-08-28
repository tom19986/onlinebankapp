package com.onlinebanking.transaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
public class TransactionDateDTO {
    @NotNull(message = "Please select start date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Please select end date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public void validateDates() {


        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be greater than start date");
        }

    }
}
