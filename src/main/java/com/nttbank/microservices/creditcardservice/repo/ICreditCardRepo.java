package com.nttbank.microservices.creditcardservice.repo;

import com.nttbank.microservices.creditcardservice.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/** Repository interface for credit card operations. */
public interface ICreditCardRepo extends ReactiveMongoRepository<CreditCard, String> {

  Mono<Long> countByCustomerIdAndStatus(String customerId, String status);
}


