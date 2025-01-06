package com.nttbank.microservices.creditcardservice.service.impl;

import com.nttbank.microservices.creditcardservice.model.CreditCard;
import com.nttbank.microservices.creditcardservice.repo.ICreditCardRepo;
import com.nttbank.microservices.creditcardservice.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link CreditCardService} to handle business logic for credit cards. This
 * service interacts with the {@link ICreditCardRepo} repository.
 */
@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

  private final ICreditCardRepo repo;

  @Override
  public Mono<CreditCard> save(CreditCard t) {
    return repo.save(t);
  }

  @Override
  public Mono<CreditCard> update(CreditCard c) {
    return repo.findById(c.getId()).flatMap(creditCardBd -> {
      CreditCard updatedCreditCard = CreditCard.builder().id(creditCardBd.getId())
          .customerId(creditCardBd.getCustomerId()).cardType(creditCardBd.getCardType())
          .creditLimit(c.getCreditLimit()).currentBalance(c.getCurrentBalance())
          .expirationDate(c.getExpirationDate()).issueStatementDay(c.getIssueStatementDay())
          .maintenanceFee(c.getMaintenanceFee()).status(c.getStatus()).build();
      return repo.save(updatedCreditCard);
    });
  }

  @Override
  public Flux<CreditCard> findAll() {
    return repo.findAll();
  }

  @Override
  public Mono<CreditCard> findById(String creditCardId) {
    return repo.findById(creditCardId);
  }

  @Override
  public Mono<Void> delete(String creditCardId) {
    return repo.deleteById(creditCardId);
  }

  @Override
  public Mono<Long> totalCreditCardsByCustomer(String customerId, String status) {
    return repo.countByCustomerIdAndStatus(customerId, status);
  }


}
