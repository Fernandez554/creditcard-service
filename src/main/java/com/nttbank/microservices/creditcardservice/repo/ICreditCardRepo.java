package com.nttbank.microservices.creditcardservice.repo;

import com.nttbank.microservices.creditcardservice.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICreditCardRepo extends ReactiveMongoRepository<CreditCard, String> {

}


