package com.ucb.farmago.backend.repositories;

import com.ucb.farmago.backend.models.AcuerdoComercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AcuerdoComercialRepository extends JpaRepository<AcuerdoComercial, Long> {
    List<AcuerdoComercial> findByActivo(Boolean activo);
}
