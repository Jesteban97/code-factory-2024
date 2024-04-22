package co.edu.udea.codefact.payments.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String number;

  @Column
  private int expirationMonth;

  @Column
  private int expirationYear;

  @Column
  private String holderName;

  @Column
  private int cvc;

  @Column
  private double balance;

  public void withdraw(double amount) throws Exception {
    double balance = this.balance - amount;
    if (balance < 0) {
      throw new Exception("Insufficient balance");
    }
    this.balance = balance;
  }
}
