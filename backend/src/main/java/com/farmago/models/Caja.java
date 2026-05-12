package com.farmago.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "caja")
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cajero_id", nullable = false)
    private Usuario cajero;

    @Column(name = "total_efectivo")
    private BigDecimal totalEfectivo = BigDecimal.ZERO;

    @Column(name = "total_qr")
    private BigDecimal totalQr = BigDecimal.ZERO;

    @Column(name = "total_general")
    private BigDecimal totalGeneral = BigDecimal.ZERO;

    private LocalDate fecha;

    private String turno;

    private Boolean cerrado = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getCajero() { return cajero; }
    public void setCajero(Usuario cajero) { this.cajero = cajero; }

    public BigDecimal getTotalEfectivo() { return totalEfectivo; }
    public void setTotalEfectivo(BigDecimal totalEfectivo) { this.totalEfectivo = totalEfectivo; }

    public BigDecimal getTotalQr() { return totalQr; }
    public void setTotalQr(BigDecimal totalQr) { this.totalQr = totalQr; }

    public BigDecimal getTotalGeneral() { return totalGeneral; }
    public void setTotalGeneral(BigDecimal totalGeneral) { this.totalGeneral = totalGeneral; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public Boolean getCerrado() { return cerrado; }
    public void setCerrado(Boolean cerrado) { this.cerrado = cerrado; }
}
