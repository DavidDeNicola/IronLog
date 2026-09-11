package org.ironlog.app.repository;

import org.ironlog.app.model.ProfiloMetabolico;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfiloMetabolicoRepository extends JpaRepository<ProfiloMetabolico, Long> {

    List<ProfiloMetabolico> findByAtletaOrderByCalcolatoIlDesc(Utente atleta);

    Optional<ProfiloMetabolico> findFirstByAtletaOrderByCalcolatoIlDesc(Utente atleta);
}
