"use client";

import { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";
import Link from "next/link";
import { getCajaById, registrarPago, cerrarCaja } from "../../../services/cajaService";
import styles from "./CajaActiva.module.css";

export default function CajaActivaPage() {
  const router = useRouter();
  const { id } = useParams();

  const [usuario] = useState(() => {
    if (typeof window === "undefined") return null;
    return JSON.parse(localStorage.getItem("usuario") || "null");
  });

  const [caja, setCaja] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState("");

  // Registro de pagos
  const [montoEfectivo, setMontoEfectivo] = useState("");
  const [montoQr, setMontoQr] = useState("");
  const [registrando, setRegistrando] = useState(false);
  const [errorPago, setErrorPago] = useState("");
  const [pagoOk, setPagoOk] = useState(false);

  // Cierre de caja
  const [cerrando, setCerrando] = useState(false);
  const [errorCierre, setErrorCierre] = useState("");

  useEffect(() => {
    if (!usuario || usuario.rol !== "CAJERO") {
      router.replace("/login");
    }
  }, [usuario, router]);

  useEffect(() => {
    if (!usuario || usuario.rol !== "CAJERO") return;

    getCajaById(id)
      .then(setCaja)
      .catch(() => setError("No se pudo cargar la información de la caja."))
      .finally(() => setCargando(false));
  }, [id, usuario]);

  const handleRegistrarPago = async () => {
    setErrorPago("");
    setPagoOk(false);

    const efectivo = montoEfectivo === "" ? 0 : parseFloat(montoEfectivo);
    const qr = montoQr === "" ? 0 : parseFloat(montoQr);

    if (isNaN(efectivo) || isNaN(qr) || efectivo < 0 || qr < 0) {
      setErrorPago("Ingresá montos válidos (mayores o iguales a 0).");
      return;
    }
    if (efectivo === 0 && qr === 0) {
      setErrorPago("Ingresá al menos un monto mayor a 0.");
      return;
    }

    setRegistrando(true);
    try {
      const cajaActualizada = await registrarPago(caja.id, efectivo, qr);
      setCaja(cajaActualizada);
      setMontoEfectivo("");
      setMontoQr("");
      setPagoOk(true);
    } catch (e) {
      setErrorPago(e.message || "No se pudo registrar el pago.");
    } finally {
      setRegistrando(false);
    }
  };

  const handleCerrarCaja = async () => {
    const confirmado = window.confirm(
      `¿Seguro que deseás cerrar la caja #${caja.id}?\n\n` +
      `Total efectivo: Bs. ${Number(caja.totalEfectivo).toFixed(2)}\n` +
      `Total QR: Bs. ${Number(caja.totalQr).toFixed(2)}\n` +
      `Gran total: Bs. ${Number(caja.totalGeneral).toFixed(2)}\n\n` +
      `Esta acción no se puede deshacer.`
    );
    if (!confirmado) return;

    setErrorCierre("");
    setCerrando(true);
    try {
      const cajaActualizada = await cerrarCaja(caja.id);
      setCaja(cajaActualizada);
    } catch (e) {
      setErrorCierre(e.message || "No se pudo cerrar la caja.");
    } finally {
      setCerrando(false);
    }
  };

  if (!usuario || usuario.rol !== "CAJERO") return null;

  return (
    <div className={styles.wrapper}>
      <main className={styles.main}>
        <div className={styles.card}>
          <h1 className={styles.title}>Caja activa</h1>

          {cargando && <p className={styles.info}>Cargando información de la caja...</p>}
          {error && <div className={styles.errorMsg}>{error}</div>}

          {caja && (
            <>
              <div className={styles.idBox}>
                <span className={styles.idLabel}>ID de caja</span>
                <span className={styles.idValue}>#{caja.id}</span>
              </div>

              <ul className={styles.detalles}>
                <li><span>Cajero</span><strong>{caja.cajero?.nombre}</strong></li>
                <li><span>Turno</span><strong>{caja.turno}</strong></li>
                <li><span>Fecha</span><strong>{caja.fecha}</strong></li>
                <li><span>Total efectivo</span><strong>Bs. {Number(caja.totalEfectivo).toFixed(2)}</strong></li>
                <li><span>Total QR</span><strong>Bs. {Number(caja.totalQr).toFixed(2)}</strong></li>
                <li><span>Total general</span><strong>Bs. {Number(caja.totalGeneral).toFixed(2)}</strong></li>
                <li>
                  <span>Estado</span>
                  <strong className={caja.cerrado ? styles.cerrada : styles.abierta}>
                    {caja.cerrado ? "Cerrada" : "Abierta"}
                  </strong>
                </li>
              </ul>

              {caja.cerrado ? (
                <div className={styles.resumenCierre}>
                  <h2 className={styles.subtitleSection}>Resumen del turno</h2>
                  <div className={styles.resumenGrid}>
                    <div className={styles.resumenItem}>
                      <span>Efectivo</span>
                      <strong>Bs. {Number(caja.totalEfectivo).toFixed(2)}</strong>
                    </div>
                    <div className={styles.resumenItem}>
                      <span>QR</span>
                      <strong>Bs. {Number(caja.totalQr).toFixed(2)}</strong>
                    </div>
                    <div className={styles.resumenItemTotal}>
                      <span>Gran total</span>
                      <strong>Bs. {Number(caja.totalGeneral).toFixed(2)}</strong>
                    </div>
                  </div>
                  <p className={styles.cierreInfo}>
                    Esta caja está cerrada y no admite más movimientos.
                  </p>
                </div>
              ) : (
                <>
                  <div className={styles.pagoSection}>
                    <h2 className={styles.subtitleSection}>Registrar pago</h2>

                    {errorPago && <div className={styles.errorMsgPago}>{errorPago}</div>}
                    {pagoOk && <div className={styles.success}>Pago registrado correctamente.</div>}

                    <div className={styles.fieldRow}>
                      <div className={styles.field}>
                        <label className={styles.label} htmlFor="montoEfectivo">Efectivo (Bs.)</label>
                        <input
                          id="montoEfectivo"
                          type="number"
                          min="0"
                          step="0.01"
                          className={styles.input}
                          placeholder="0.00"
                          value={montoEfectivo}
                          onChange={(e) => setMontoEfectivo(e.target.value)}
                        />
                      </div>
                      <div className={styles.field}>
                        <label className={styles.label} htmlFor="montoQr">QR (Bs.)</label>
                        <input
                          id="montoQr"
                          type="number"
                          min="0"
                          step="0.01"
                          className={styles.input}
                          placeholder="0.00"
                          value={montoQr}
                          onChange={(e) => setMontoQr(e.target.value)}
                        />
                      </div>
                    </div>

                    <button className={styles.btn} onClick={handleRegistrarPago} disabled={registrando}>
                      {registrando ? "Registrando..." : "Registrar pago"}
                    </button>
                  </div>

                  <div className={styles.cierreSection}>
                    {errorCierre && <div className={styles.errorMsgPago}>{errorCierre}</div>}
                    <button className={styles.btnDanger} onClick={handleCerrarCaja} disabled={cerrando}>
                      {cerrando ? "Cerrando caja..." : "Cerrar caja"}
                    </button>
                  </div>
                </>
              )}

              <div className={styles.actions}>
                <Link href="/caja/historial" className={styles.linkSecundario}>Ver historial de cajas</Link>
                {caja.cerrado && (
                  <Link href="/caja/abrir" className={styles.linkSecundario}>Abrir nueva caja</Link>
                )}
              </div>
            </>
          )}
        </div>
      </main>
    </div>
  );
}