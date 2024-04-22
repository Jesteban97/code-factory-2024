package co.edu.udea.codefact.payments.api;

import co.edu.udea.codefact.payments.api.dto.CreateCreditCardRequestDto;
import co.edu.udea.codefact.payments.domain.CreditCard;
import co.edu.udea.codefact.payments.infrastructure.ICreditCardRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/credit-cards")
public class CreditCardController {

  private final ICreditCardRepository creditCardRepository;

  CreditCardController(ICreditCardRepository creditCardRepository) {
    this.creditCardRepository = creditCardRepository;
  }

  @GetMapping
  public ResponseEntity<List<CreditCard>> getAllCreditCards() {
    Iterable<CreditCard> iterable = this.creditCardRepository.findAll();
    List<CreditCard> creditCards = StreamSupport.stream(
      iterable.spliterator(),
      false
    ).collect(Collectors.toList());
    return ResponseEntity.ok(creditCards);
  }

  @PostMapping
  public ResponseEntity<CreditCard> createCreditCard(
    @RequestBody CreateCreditCardRequestDto createCreditCardRequest
  ) {
    Optional<CreditCard> optional =
      this.creditCardRepository.findByNumberAndCvc(
          createCreditCardRequest.getNumber(),
          createCreditCardRequest.getCvc()
        );
    if (optional.isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    CreditCard creditCardToSave = CreditCard.builder()
      .number(createCreditCardRequest.getNumber())
      .expirationMonth(createCreditCardRequest.getExpirationMonth())
      .expirationYear(createCreditCardRequest.getExpirationYear())
      .holderName(createCreditCardRequest.getHolderName())
      .cvc(createCreditCardRequest.getCvc())
      .balance(createCreditCardRequest.getBalance())
      .build();
    CreditCard createdCreditCard =
      this.creditCardRepository.save(creditCardToSave);
    return ResponseEntity.ok(createdCreditCard);
  }
}
