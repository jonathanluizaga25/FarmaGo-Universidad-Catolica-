"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { getCajasByCajero } from "../../../services/cajaService";
import styles from "./Historial.module.css";

export default function HistorialCajasPage() {
  const router = useRouter();

  const [usuario] = useState(() => {
    if (typeof window === "undefined") return null;
    return JSON.parse(localStorage.getItem("usuario") || "null");
  });

  const [cajas, setCajas] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!usuario || usuario.rol !== "CAJERO") {
      router.replace("/login");
    }
  }, [usuario, router]);

  useEffect(() => {
    if (!usuario || usuario.rol !== "CAJERO") return;

    getCajasByCajero(usuario.id)
      .then((data) => {
        const ordenadas = [...data].sort((a, b) => {
          if (a.fecha !== b.fecha) return a.fecha < b.fecha ? 1 : -1;
          return b.id - a.id;
        });
        setCajas(ordenadas);
      })
      .catch(() => setError("No se pudo cargar el historial de cajas."))
      .finally(() => setCargando(false));
  }, [usuario]);

  if (!usuario || usuario.rol !== "CAJERO") return null;

  return (
    <div className={styles.wrapper}>
      <main className={styles.main}>
        <div className={styles.card}>
          <h1 className={styles.title}>Historial de cajas</h1>
          <p className={styles.subtitle}>
            Cajas que abriste como <strong>{usuario.nombre}</strong>.
          </p>

          {cargando && <p className={styles.info}>Cargando historial...</p>}
          {error && <div className={styles.errorMsg}>{error}</div>}

          {!cargando && !error && cajas.length === 0 && (
            <p className={styles.info}>Todavía no abriste ninguna caja.</p>
          )}

          {cajas.length > 0 && (
            <ul className={styles.lista}>
              {cajas.map((c) => (
                <li key={c.id} className={styles.item}>
                  <Link href={`/caja/${c.id}`} className={styles.itemLink}>
                    <div className={styles.itemHeader}>
                      <span className={styles.itemId}>Caja #{c.id}</span>
                      <span className={c.cerrado ? styles.badgeCerrada : styles.badgeAbierta}>
                        {c.cerrado ? "Cerrada" : "Abierta"}
                      </span>
                    </div>
                    <div className={styles.itemBody}>
                      <span>{c.fecha} · Turno {c.turno}</span>
                      <strong>Bs. {Number(c.totalGeneral).toFixed(2)}</strong>
                    </div>
                  </Link>
                </li>
              ))}
            </ul>
          )}

          <div className={styles.actions}>
            <Link href="/caja/abrir" className={styles.linkSecundario}>Abrir / ir a mi caja</Link>
          </div>
        </div>
      </main>
    </div>
  );
}