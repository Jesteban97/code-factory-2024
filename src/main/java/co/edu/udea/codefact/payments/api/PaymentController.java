package co.edu.udea.codefact.payments.api;

import co.edu.udea.codefact.payments.api.dto.CreatePaymentRequestDto;
import co.edu.udea.codefact.payments.api.dto.PaymentResponseDto;
import co.edu.udea.codefact.payments.domain.Payment;
import co.edu.udea.codefact.payments.infrastructure.PaymentRepository;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

  private final PaymentRepository paymentRepository;

  PaymentController(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  @PostMapping("/payments")
  public PaymentResponseDto payReservation(
    @RequestBody CreatePaymentRequestDto createPaymentRequest
  ) {
    Payment paymentToSave = Payment.builder()
      .name(createPaymentRequest.getName())
      .build();
    Payment createdPayment = this.paymentRepository.save(paymentToSave);
    PaymentResponseDto paymentToResponse = PaymentResponseDto.builder()
      .id(createdPayment.getId())
      .name(createdPayment.getName())
      .build();
    return paymentToResponse;
  }
}
