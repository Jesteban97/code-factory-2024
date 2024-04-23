package co.edu.udea.codefact.payments.infrastructure;

import co.edu.udea.codefact.payments.domain.Reserva;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservaRepository extends JpaRepository<Reserva, Long> {}
