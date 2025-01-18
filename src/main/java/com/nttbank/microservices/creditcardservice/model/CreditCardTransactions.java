package com.nttbank.microservices.creditcardservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "creditcard_transactions")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class CreditCardTransactions {
  @EqualsAndHashCode.Include
  @Id
  private String id;

  private String customerId;

  private String creditCardId;

  @NotNull(message = "Transaction type cannot be null.")
  private String type;

  @DecimalMin(value = "0.00", message = "Amount must be greater than 0.")
  @NotNull(message = "Amount cannot be null.")
  private BigDecimal amount;

  private BigDecimal balanceAfterMovement;

  @NotNull(message = "Timestamp cannot be null.")
  private LocalDateTime createdAt;

  @Size(max = 255, message = "Description cannot exceed 255 characters.")
  private String description;
}
