"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { abrirCaja, getCajaActiva } from "../../../services/cajaService";
import styles from "./AbrirCaja.module.css";

const TURNOS = [
  { value: "MANANA", label: "Mañana" },
  { value: "TARDE", label: "Tarde" },
  { value: "NOCHE", label: "Noche" },
  { value: "DIA_COMPLETO", label: "Día completo" },
];

export default function AbrirCajaPage() {
  const router = useRouter();

  const [usuario] = useState(() => {
    if (typeof window === "undefined") return null;
    return JSON.parse(localStorage.getItem("usuario") || "null");
  });

  const [verificando, setVerificando] = useState(true);
  const [cajaActiva, setCajaActiva] = useState(null);
  const [turno, setTurno] = useState("MANANA");
  const [abriendo, setAbriendo] = useState(false);
  const [error, setError] = useState("");
  const [cajaCreada, setCajaCreada] = useState(null);

  // Solo CAJERO puede acceder
  useEffect(() => {
    if (!usuario || usuario.rol !== "CAJERO") {
      router.replace("/login");
    }
  }, [usuario, router]);

  // Verificar si ya tiene una caja abierta
  useEffect(() => {
    if (!usuario || usuario.rol !== "CAJERO") return;

    getCajaActiva(usuario.id)
      .then((caja) => setCajaActiva(caja))
      .catch(() => setError("No se pudo verificar el estado de tu caja."))
      .finally(() => setVerificando(false));
  }, [usuario]);

  // Redirigir si ya hay caja abierta
  useEffect(() => {
    if (!cajaActiva) return;
    const t = setTimeout(() => router.push(`/caja/${cajaActiva.id}`), 1800);
    return () => clearTimeout(t);
  }, [cajaActiva, router]);

  // Redirigir tras crear la caja
  useEffect(() => {
    if (!cajaCreada) return;
    const t = setTimeout(() => router.push(`/caja/${cajaCreada.id}`), 1500);
    return () => clearTimeout(t);
  }, [cajaCreada, router]);

  const handleAbrir = async () => {
    setError("");
    setAbriendo(true);
    try {
      const caja = await abrirCaja(usuario.id, turno);
      setCajaCreada(caja);
    } catch (e) {
      setError(e.message || "No se pudo abrir la caja.");
    } finally {
      setAbriendo(false);
    }
  };

  if (!usuario || usuario.rol !== "CAJERO") return null;

  return (
    <div className={styles.wrapper}>
      <main className={styles.main}>
        <div className={styles.card}>
          <h1 className={styles.title}>Apertura de caja</h1>
          <p className={styles.subtitle}>
            Hola, <strong>{usuario.nombre}</strong>. Abre tu caja para registrar los pagos del turno.
          </p>

          {verificando && <p className={styles.info}>Verificando estado de tu caja...</p>}

          {!verificando && cajaActiva && (
            <div className={styles.notice}>
              Ya tenés una caja abierta: <strong>#{cajaActiva.id}</strong> (Turno {cajaActiva.turno}).
              Te estamos redirigiendo...
            </div>
          )}

          {!verificando && !cajaActiva && !cajaCreada && (
            <>
              {error && <div className={styles.errorMsg}>{error}</div>}

              <div className={styles.field}>
                <label className={styles.label} htmlFor="turno">Turno</label>
                <select
                  id="turno"
                  className={styles.select}
                  value={turno}
                  onChange={(e) => setTurno(e.target.value)}
                >
                  {TURNOS.map((t) => (
                    <option key={t.value} value={t.value}>{t.label}</option>
                  ))}
                </select>
              </div>

              <button className={styles.btn} onClick={handleAbrir} disabled={abriendo}>
                {abriendo ? "Abriendo caja..." : "Abrir caja"}
              </button>
            </>
          )}

          {cajaCreada && (
            <div className={styles.success}>
              Caja <strong>#{cajaCreada.id}</strong> abierta correctamente (Turno {cajaCreada.turno}).
              Redirigiendo...
            </div>
          )}
        </div>
      </main>
    </div>
  );
}