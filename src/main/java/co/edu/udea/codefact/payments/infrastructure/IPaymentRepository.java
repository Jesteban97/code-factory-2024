package co.edu.udea.codefact.payments.infrastructure;

import co.edu.udea.codefact.payments.domain.Payment;
import org.springframework.data.repository.CrudRepository;

public interface IPaymentRepository extends CrudRepository<Payment, Long> {}
