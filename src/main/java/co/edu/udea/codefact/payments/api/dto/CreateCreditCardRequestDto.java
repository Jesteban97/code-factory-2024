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
public class CreateCreditCardRequestDto {

  private String number;

  private int expirationMonth;

  private int expirationYear;

  private String holderName;

  private int cvc;

  private double balance;
}
