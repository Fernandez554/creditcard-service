package com.nttbank.microservices.creditcardservice.repo;

import com.nttbank.microservices.creditcardservice.model.CreditCardTransactions;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ICreditCardTransactionRepo extends
    ReactiveMongoRepository<CreditCardTransactions, String> {

  Flux<CreditCardTransactions> findAllByCreditCardId(String creditCardId);

}
