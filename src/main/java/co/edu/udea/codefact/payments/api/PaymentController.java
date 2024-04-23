package co.edu.udea.codefact.payments.api;

import co.edu.udea.codefact.payments.api.dto.CreatePaymentRequestDto;
import co.edu.udea.codefact.payments.domain.CreditCard;
import co.edu.udea.codefact.payments.domain.Payment;
import co.edu.udea.codefact.payments.domain.Reserva;
import co.edu.udea.codefact.payments.infrastructure.ICreditCardRepository;
import co.edu.udea.codefact.payments.infrastructure.IPaymentRepository;
import co.edu.udea.codefact.payments.infrastructure.IReservaRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Random;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

  private final IReservaRepository reservaRepository;
  private final IPaymentRepository paymentRepository;
  private final ICreditCardRepository creditCardRepository;

  PaymentController(
    IReservaRepository reservaRepository,
    IPaymentRepository paymentRepository,
    ICreditCardRepository creditCardRepository
  ) {
    this.reservaRepository = reservaRepository;
    this.paymentRepository = paymentRepository;
    this.creditCardRepository = creditCardRepository;
  }

  @Transactional
  @PostMapping
  public ResponseEntity<Payment> payReservation(
    @RequestBody CreatePaymentRequestDto createPaymentRequest
  ) {
    Optional<CreditCard> optionalCreditCard =
      this.creditCardRepository.findByNumberAndCvc(
          createPaymentRequest.getCardNumber(),
          createPaymentRequest.getCardCvc()
        );
    if (!optionalCreditCard.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    // TODO: make this dynamic.
    final Long reservationId = 1L;
    Optional<Reserva> optionalReservation =
      this.reservaRepository.findById(reservationId);
    if (!optionalReservation.isPresent()) {
      // TODO: return ResponseEntity.notFound().build();
    }
    final var rand = new Random();
    final var amount = rand.nextInt(5000000 - 1000000 + 1) + 1000000;
    CreditCard creditCard = optionalCreditCard.get();
    try {
      creditCard.withdraw(amount);
    } catch (Exception e) {
      if (optionalReservation.isPresent()) {
        Reserva reservation = optionalReservation.get();
        reservation.setEstado("RECHAZADO");
        this.reservaRepository.save(reservation);
      }
      return ResponseEntity.badRequest().build();
    }
    if (optionalReservation.isPresent()) {
      Reserva reservation = optionalReservation.get();
      reservation.setEstado("APROVADO");
      this.reservaRepository.save(reservation);
    }
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
