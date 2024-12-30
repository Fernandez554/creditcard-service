package com.nttbank.microservices.creditcardservice.service.impl;

import com.nttbank.microservices.creditcardservice.model.CreditCard;
import com.nttbank.microservices.creditcardservice.repo.ICreditCardRepo;
import com.nttbank.microservices.creditcardservice.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

  private final ICreditCardRepo repo;

  @Override
  public Mono<CreditCard> save(CreditCard t) {
    return repo.save(t);
  }

  @Override
  public Mono<CreditCard> update(CreditCard t) {
    return repo.save(t);
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
}
