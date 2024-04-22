package co.edu.udea.codefact.payments.api;

import co.edu.udea.codefact.payments.api.dto.CreatePaymentRequestDto;
import co.edu.udea.codefact.payments.domain.CreditCard;
import co.edu.udea.codefact.payments.domain.Payment;
import co.edu.udea.codefact.payments.infrastructure.ICreditCardRepository;
import co.edu.udea.codefact.payments.infrastructure.IPaymentRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Random;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  private final IPaymentRepository paymentRepository;
  private final ICreditCardRepository creditCardRepository;

  PaymentController(
    IPaymentRepository paymentRepository,
    ICreditCardRepository creditCardRepository
  ) {
    this.paymentRepository = paymentRepository;
    this.creditCardRepository = creditCardRepository;
  }

  @Transactional
  @PostMapping
  public ResponseEntity<Payment> payReservation(
    @RequestBody CreatePaymentRequestDto createPaymentRequest
  ) {
    Optional<CreditCard> optional =
      this.creditCardRepository.findByNumberAndCvc(
          createPaymentRequest.getCardNumber(),
          createPaymentRequest.getCardCvc()
        );
    if (!optional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    final var rand = new Random();
    final var amount = rand.nextInt(5000000 - 1000000 + 1) + 1000000;
    CreditCard creditCard = optional.get();
    try {
      creditCard.withdraw(amount);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    // TODO: After successfully withdrawn, the reservation state should change
    // to "APPROVED".
    this.creditCardRepository.save(creditCard);
    final var owner = createPaymentRequest.getCardHolderName();
    final var paymentToSave = Payment.builder()
      .amount(amount)
      .owner(owner)
      .build();
    final var createdPayment = this.paymentRepository.save(paymentToSave);
    return ResponseEntity.ok(createdPayment);
  }
}
