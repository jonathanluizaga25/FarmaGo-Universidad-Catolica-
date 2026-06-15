"use client";
import Link from "next/link";
import Image from "next/image";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import styles from "./Navbar.css";

export default function Navbar() {
  const [searchValue, setSearchValue] = useState("");
  const [usuario, setUsuario] = useState(null);
  const router = useRouter();

  useEffect(() => {
    const u = localStorage.getItem("usuario");
    if (u) setUsuario(JSON.parse(u));
  }, []);

  function cerrarSesion() {
    localStorage.removeItem("usuario");
    setUsuario(null);
    router.push("/");
  }

  return (
    <>
      {/* ── Barra de navegación azul ── */}
      <nav className="navbar-main">
        <div className="navbar-main__inner">

          {/* Logo */}
          <div className="navbar-main__brand">
            <Image
              src="/logo.png"
              alt="FarmaGO"
              width={52}
              height={52}
              style={{ objectFit: "contain" }}
            />
          </div>

          {/* Links */}
          <ul className="navbar-main__links">
            <li>
              <Link href="/" className="nav-link active">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                  <polyline points="9 22 9 12 15 12 15 22" />
                </svg>
                inicio
              </Link>
            </li>

            <li>
              <Link href="/catalogo" className="nav-link">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <rect x="3" y="3" width="7" height="7" />
                  <rect x="14" y="3" width="7" height="7" />
                  <rect x="14" y="14" width="7" height="7" />
                  <rect x="3" y="14" width="7" height="7" />
                </svg>
                catalogo
              </Link>
            </li>

            <li>
              <Link href="contactos" className="nav-link">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12 19.79 19.79 0 0 1 1.61 3.35 2 2 0 0 1 3.6 1h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L7.91 8.6a16 16 0 0 0 6 6l.92-.92a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16" />
                </svg>
                contactos
              </Link>
            </li>
          </ul>

          {/* ── Botones auth + carrito ── */}
          <div className="navbar-main__auth">

            {/* Carrito — solo si está logueado */}
            {usuario && (
              <Link href="/carrito" className="auth-btn auth-btn--outline">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <circle cx="9" cy="21" r="1" />
                  <circle cx="20" cy="21" r="1" />
                  <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6" />
                </svg>
                Carrito
              </Link>
            )}

            {usuario ? (
              <>
                <span className="auth-btn auth-btn--outline" style={{ cursor: "default" }}>
                  Hola, {usuario.nombre?.split(" ")[0]}
                </span>
                <button className="auth-btn auth-btn--solid" onClick={cerrarSesion}>
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
                    <polyline points="16 17 21 12 16 7" />
                    <line x1="21" y1="12" x2="9" y2="12" />
                  </svg>
                  Cerrar sesión
                </button>
              </>
            ) : (
              <>
                <Link href="/registro" className="auth-btn auth-btn--outline">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
                    <circle cx="9" cy="7" r="4" />
                    <line x1="19" y1="8" x2="19" y2="14" />
                    <line x1="22" y1="11" x2="16" y2="11" />
                  </svg>
                  Registrarse
                </Link>
                <Link href="/login" className="auth-btn auth-btn--solid">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4" />
                    <polyline points="10 17 15 12 10 7" />
                    <line x1="15" y1="12" x2="3" y2="12" />
                  </svg>
                  Iniciar sesión
                </Link>
              </>
            )}

            {/* ENLACE REQUERIDO POR LA HISTORIA DE USUARIO */}
            <Link href="/seguimiento" className="auth-btn auth-btn--outline">
              Mis Pedidos
            </Link>

          </div>
        </div>
      </nav>
    </>
  );
}
