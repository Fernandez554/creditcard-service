package com.nttbank.microservices.creditcardservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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

  @Id
  private String id;
  private String customerId;
  private String cardType;
  private BigDecimal creditLimit;
  private BigDecimal currentBalance;
  private String status;
  private LocalDate expirationDate;
  private LocalDate issueDate;
  private BigDecimal interestRate;
}
