package co.edu.udea.codefact.payments.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequestDto {

  private Long reservationId;

  private String cardNumber;

  private int cardCvc;

  private String cardHolderName;
}
