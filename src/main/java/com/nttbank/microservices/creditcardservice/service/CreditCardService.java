package com.nttbank.microservices.creditcardservice.service;

import com.nttbank.microservices.creditcardservice.model.CreditCard;
import java.math.BigDecimal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Service interface for credit card operations. */
public interface CreditCardService {

  Mono<CreditCard> save(CreditCard t);

  Mono<CreditCard> update(CreditCard t);

  Flux<CreditCard> findAll();

  Mono<CreditCard> findById(String creditCardId);

  Mono<Void> delete(String creditCardId);

  Mono<Long> totalCreditCardsByCustomer(String customerId, String status);

  Mono<CreditCard> chargeCreditCard(String creditCardId, BigDecimal amount);

  Mono<CreditCard> payCreditCard(String creditCardId, BigDecimal amount);
}
