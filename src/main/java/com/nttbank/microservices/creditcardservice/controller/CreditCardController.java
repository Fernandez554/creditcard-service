package com.nttbank.microservices.creditcardservice.controller;

import com.nttbank.microservices.creditcardservice.model.CreditCard;
import com.nttbank.microservices.creditcardservice.service.CreditCardService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/creditcards")
@RequiredArgsConstructor
@Validated
public class CreditCardController {

  private final CreditCardService service;

  @GetMapping
  public Mono<ResponseEntity<Flux<CreditCard>>> findAll() {
    Flux<CreditCard> creditCardList = service.findAll();

    return Mono.just(
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(creditCardList))
        .defaultIfEmpty(ResponseEntity.noContent().build());
  }

  @GetMapping("/{creditcard_id}")
  public Mono<ResponseEntity<CreditCard>> findById(
      @Valid @PathVariable("creditcard_id") String id) {
    return service.findById(id)
        .map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<ResponseEntity<CreditCard>> save(@Valid @RequestBody CreditCard creditCard,
      final ServerHttpRequest req) {
    return service.save(creditCard).map(c -> ResponseEntity.created(
                URI.create(req.getURI().toString().concat("/").concat(c.getId())))
            .contentType(MediaType.APPLICATION_JSON).body(c))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping("/{creditcard_id}")
  public Mono<ResponseEntity<CreditCard>> update(@Valid @PathVariable("creditcard_id") String id,
      @Valid @RequestBody CreditCard creditCard) {
    creditCard.setId(id);

    Mono<CreditCard> monoBody = Mono.just(creditCard);
    Mono<CreditCard> monoDb = service.findById(id);

    return monoDb.zipWith(monoBody, (db, c) -> {
      db.setId(id);
      db.setCustomerId(c.getCustomerId());
      db.setCardType(c.getCardType());
      db.setCreditLimit(c.getCreditLimit());
      db.setCurrentBalance(c.getCurrentBalance());
      db.setStatus(c.getStatus());
      db.setExpirationDate(c.getExpirationDate());
      db.setIssueDate(c.getIssueDate());
      db.setInterestRate(c.getInterestRate());
      return db;
    }).flatMap(service::update)
      .map(e -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{creditcard_id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable("creditcard_id") String id) {
    return service.findById(id).flatMap(
            c -> service.delete(c.getId()).thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

}
