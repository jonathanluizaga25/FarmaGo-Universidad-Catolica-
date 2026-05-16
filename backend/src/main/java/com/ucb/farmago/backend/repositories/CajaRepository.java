package com.ucb.farmago.backend.repositories;

import com.ucb.farmago.backend.models.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    List<Caja> findByCajeroId(Long cajeroId);
    List<Caja> findByCerrado(Boolean cerrado);
}
