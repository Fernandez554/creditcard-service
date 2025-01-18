package com.nttbank.microservices.creditcardservice.dto;

import com.nttbank.microservices.creditcardservice.model.CreditCardTransactions;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreditCardDTO class that represents the credit card collection.
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
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<CreditCardTransactions> lstCreditCardTransactions;


}
