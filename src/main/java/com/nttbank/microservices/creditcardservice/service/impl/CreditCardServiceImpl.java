package com.nttbank.microservices.creditcardservice.service.impl;

import com.nttbank.microservices.creditcardservice.model.CreditCard;
import com.nttbank.microservices.creditcardservice.model.CreditCardTransactions;
import com.nttbank.microservices.creditcardservice.repo.ICreditCardRepo;
import com.nttbank.microservices.creditcardservice.repo.ICreditCardTransactionRepo;
import com.nttbank.microservices.creditcardservice.service.CreditCardService;
import com.nttbank.microservices.creditcardservice.util.Constants;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link CreditCardService} to handle business logic for credit cards. This
 * service interacts with the {@link ICreditCardRepo} repository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

  private final ICreditCardRepo creditCardRepo;
  private final ICreditCardTransactionRepo transactionRepo;

  @Override
  public Mono<CreditCard> save(CreditCard t) {
    return creditCardRepo.save(t);
  }

  @Override
  public Mono<CreditCard> update(CreditCard c) {
    return creditCardRepo.findById(c.getId()).flatMap(creditCardBd -> {
      CreditCard updatedCreditCard = CreditCard.builder().id(creditCardBd.getId())
          .customerId(creditCardBd.getCustomerId()).cardType(creditCardBd.getCardType())
          .creditLimit(c.getCreditLimit()).currentBalance(c.getCurrentBalance())
          .expirationDate(c.getExpirationDate()).issueStatementDay(c.getIssueStatementDay())
          .maintenanceFee(c.getMaintenanceFee()).status(c.getStatus()).build();
      return creditCardRepo.save(updatedCreditCard);
    });
  }

  @Override
  public Flux<CreditCard> findAll() {
    return creditCardRepo.findAll();
  }

  @Override
  public Mono<CreditCard> findById(String creditCardId) {
    return creditCardRepo.findById(creditCardId).flatMap(creditCard ->
        transactionRepo.findAllByCreditCardId(creditCardId)
            .collectList()
            .map(transactions -> {
              creditCard.setLstCreditCardTransactions(transactions);
              return creditCard;
            })
    );
  }

  @Override
  public Mono<Void> delete(String creditCardId) {
    return creditCardRepo.deleteById(creditCardId);
  }

  @Override
  public Mono<Long> totalCreditCardsByCustomer(String customerId, String status) {
    return creditCardRepo.countByCustomerIdAndStatus(customerId, status);
  }

  @Override
  public Mono<CreditCard> chargeCreditCard(String creditCardId, BigDecimal amount) {
    return findById(creditCardId)
        .flatMap(creditCard -> {
          return validateCreditCardFunds(creditCard, amount)
              .flatMap(this::save)
              .flatMap(cc -> saveTransaction(cc, amount, Constants.CHARGE))
              .flatMap(cc -> {
                cc.setLstCreditCardTransactions(null);
                return Mono.just(cc);
              });
        });
  }

  @Override
  public Mono<CreditCard> payCreditCard(String creditCardId, BigDecimal amountPaid) {
    return findById(creditCardId)
        .flatMap(creditCard -> {
          return validateAmountToPay(creditCard, amountPaid)
              .flatMap(this::save)
              .flatMap(cc -> saveTransaction(cc, amountPaid, Constants.PAYMENT))
              .flatMap(cc -> {
                cc.setLstCreditCardTransactions(null);
                return Mono.just(cc);
              });
        });
  }

  private Mono<CreditCard> validateAmountToPay(CreditCard creditCard, BigDecimal amountPaid) {
    BigDecimal totalPaid = creditCard.getCurrentBalance()
        .add(amountPaid)
        .setScale(2, RoundingMode.HALF_UP);
    return Mono.just(creditCard)
        .filter(cc -> cc.getCreditLimit().compareTo(totalPaid) >= 0)
        .flatMap(filteredCreditCard -> {
          BigDecimal newBalance = filteredCreditCard.getCurrentBalance()
              .add(amountPaid)
              .setScale(2, RoundingMode.HALF_UP);
          filteredCreditCard.setCurrentBalance(newBalance);
          return Mono.just(filteredCreditCard);
        })
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Cannot pay more than the total amount owed (including credit limit).")));
  }

  private Mono<CreditCard> validateCreditCardFunds(CreditCard creditCard, BigDecimal amount) {
    return Mono.just(creditCard)
        .filter(cc -> cc.getCurrentBalance().compareTo(amount) >= 0)
        .flatMap(cc -> {
          BigDecimal newBalance = cc.getCurrentBalance()
              .subtract(amount)
              .setScale(2, RoundingMode.HALF_UP);
          cc.setCurrentBalance(newBalance);
          return Mono.just(cc);
        })
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Insufficient funds in the credit card.")));
  }

  private Mono<CreditCard> saveTransaction(CreditCard creditCard, BigDecimal amount,
      String action) {
    log.info("Saving the {} transaction.", action);
    return transactionRepo.save(CreditCardTransactions.builder()
        .customerId(creditCard.getCustomerId())
        .creditCardId(creditCard.getId())
        .balanceAfterMovement(creditCard.getCurrentBalance())
        .amount(amount)
        .type(action)
        .createdAt(LocalDateTime.now())
        .build()
    ).thenReturn(creditCard);
  }


}
