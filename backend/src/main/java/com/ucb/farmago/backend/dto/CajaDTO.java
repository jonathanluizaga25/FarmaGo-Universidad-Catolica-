package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Caja;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CajaDTO {

    private Long id;
    private UsuarioDTO cajero;
    private BigDecimal totalEfectivo;
    private BigDecimal totalQr;
    private BigDecimal totalGeneral;
    private LocalDate fecha;
    private String turno;
    private Boolean cerrado;

    public CajaDTO(Caja caja) {
        this.id = caja.getId();
        this.cajero = caja.getCajero() != null ? new UsuarioDTO(caja.getCajero()) : null;
        this.totalEfectivo = caja.getTotalEfectivo();
        this.totalQr = caja.getTotalQr();
        this.totalGeneral = caja.getTotalGeneral();
        this.fecha = caja.getFecha();
        this.turno = caja.getTurno();
        this.cerrado = caja.getCerrado();
    }

    public Long getId() { return id; }
    public UsuarioDTO getCajero() { return cajero; }
    public BigDecimal getTotalEfectivo() { return totalEfectivo; }
    public BigDecimal getTotalQr() { return totalQr; }
    public BigDecimal getTotalGeneral() { return totalGeneral; }
    public LocalDate getFecha() { return fecha; }
    public String getTurno() { return turno; }
    public Boolean getCerrado() { return cerrado; }
}
