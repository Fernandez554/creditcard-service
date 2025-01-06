package com.nttbank.microservices.creditcardservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a credit card with various attributes including card type, customer ID, creditLimit,
 * currentBalance, and other properties. This class is used to interact with the 'credit_cards'
 * collection in the MongoDB database.
 */
@Data
@Document(collection = "credit_cards")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class CreditCard {

  @EqualsAndHashCode.Include
  @Id
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
