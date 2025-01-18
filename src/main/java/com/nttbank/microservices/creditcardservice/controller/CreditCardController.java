package com.nttbank.microservices.creditcardservice.controller;

import com.nttbank.microservices.creditcardservice.dto.CreditCardDTO;
import com.nttbank.microservices.creditcardservice.mapper.CreditCardMapper;
import com.nttbank.microservices.creditcardservice.model.CreditCard;
import com.nttbank.microservices.creditcardservice.service.CreditCardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller class to handle HTTP requests for credit card operations.
 */
@RestController
@RequestMapping("/creditcards")
@RequiredArgsConstructor
@Validated
public class CreditCardController {

  private final CreditCardService service;
  private final CreditCardMapper mapper;
  private static final Logger logger = LoggerFactory.getLogger(CreditCardController.class);

  @GetMapping
  public Mono<ResponseEntity<Flux<CreditCard>>> findAll() {
    return Mono.just(
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()))
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
  public Mono<ResponseEntity<CreditCard>> save(@Valid @RequestBody CreditCardDTO creditCardDTO,
      final ServerHttpRequest req) {
    return service.save(mapper.creditCardDTOToCreditCard(creditCardDTO))
        .map(c -> ResponseEntity.created(
                URI.create(req.getURI().toString().concat("/").concat(c.getId())))
            .contentType(MediaType.APPLICATION_JSON).body(c))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping
  public Mono<ResponseEntity<CreditCard>> update(
      @Valid @RequestBody CreditCardDTO creditCardDTO) {
    return service.update(mapper.creditCardDTOToCreditCard(creditCardDTO))
        .map(e -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{creditcard_id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable("creditcard_id") String id) {
    return service.findById(id)
        .flatMap(c -> service.delete(c.getId())
            .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  //TODO: Cambiar el path de la URL
  @GetMapping("/{customer_id}/count")
  public Mono<ResponseEntity<Long>> totalCreditCardsByCustomer(
      @PathVariable("customer_id") String customerId,
      @RequestParam("status") @NotNull String status) {
    logger.info("Fetching total credit cards by customer");
    return service.totalCreditCardsByCustomer(customerId, status)
        .map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping("/{creditcard_id}/charge")
  public Mono<ResponseEntity<CreditCard>> charge(
      @PathVariable("creditcard_id") String creditCardId,
      @QueryParam("amount") @NotNull BigDecimal amount) {
    return service.chargeCreditCard(creditCardId, amount)
        .map(e -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping("/{creditcard_id}/pay")
  public Mono<ResponseEntity<CreditCard>> pay(@PathVariable("creditcard_id") String creditCardId,
      @QueryParam("amount") @NotNull BigDecimal amount) {
    return service.payCreditCard(creditCardId, amount)
        .map(e -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }


}
