"use client";

import { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";
import { getCajaById } from "../../../services/cajaService";
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
            </>
          )}
        </div>
      </main>
    </div>
  );
}