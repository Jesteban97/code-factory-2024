package co.edu.udea.codefact.payments.infrastructure;

import co.edu.udea.codefact.payments.domain.CreditCard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICreditCardRepository extends JpaRepository<CreditCard, Long> {
  Optional<CreditCard> findByNumberAndCvc(String number, int cvc);
}
