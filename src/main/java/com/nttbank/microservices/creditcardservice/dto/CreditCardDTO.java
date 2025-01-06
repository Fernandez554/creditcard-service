package com.nttbank.microservices.creditcardservice.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Represents a credit card with various attributes including credit card ID, customer ID, status,
 * credit card number, expiration date, CVV, credit limit, and available credit. This class is used
 * to interact with the 'credit_cards' collection in the MongoDB database.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCardDTO {

  private String id;

  @NotNull(message = "Customer Identifier cannot be null")
  private String customerId;

  private String cardType;

  @NotNull(message = "Credit Limit cannot be null")
  private BigDecimal creditLimit;
  private BigDecimal currentBalance;
  @NotNull(message = "Expiration Date cannot be null")
  private LocalDate expirationDate;

  @NotNull(message = "Issue Statement Day cannot be null")
  private String issueStatementDay;

  @NotNull(message = "Maintenance Fee cannot be null")
  private BigDecimal maintenanceFee;
  private String status;

}
