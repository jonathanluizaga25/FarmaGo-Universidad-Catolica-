import Image from "next/image";
import Navbar from "../../../components/Navbar/Navbar";
import "./page.css";

export default function Contactos() {
  const objetivos = [
    {
      icon: (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
          <rect x="2" y="3" width="20" height="14" rx="2" />
          <path d="M8 21h8M12 17v4" />
        </svg>
      ),
      titulo: "Ventas unificadas",
      desc: "Gestionamos tus compras presenciales y en línea bajo un mismo inventario, siempre actualizado en tiempo real.",
    },
    {
      icon: (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
          <circle cx="12" cy="12" r="10" />
          <path d="M12 6v6l4 2" />
        </svg>
      ),
      titulo: "Seguimiento en tiempo real",
      desc: "Cada pedido tiene trazabilidad completa: desde que sale de la farmacia hasta llegar a tu puerta.",
    },
    {
      icon: (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
          <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
        </svg>
      ),
      titulo: "Productos verificados",
      desc: "Controlamos fechas de vencimiento y autenticidad de cada lote. Solo recibís productos en perfecto estado.",
    },
    {
      icon: (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
          <rect x="1" y="3" width="15" height="13" rx="2" />
          <path d="M16 8h4l3 5v3h-7V8z" />
          <circle cx="5.5" cy="18.5" r="2.5" />
          <circle cx="18.5" cy="18.5" r="2.5" />
        </svg>
      ),
      titulo: "Entrega a domicilio",
      desc: "Pedí desde donde estés y recibí tus medicamentos y productos de cuidado personal sin moverte de casa.",
    },
    {
      icon: (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
          <polyline points="14 2 14 8 20 8" />
          <line x1="16" y1="13" x2="8" y2="13" />
          <line x1="16" y1="17" x2="8" y2="17" />
          <polyline points="10 9 9 9 8 9" />
        </svg>
      ),
      titulo: "Facturación automática",
      desc: "Generamos tu factura digital al instante y te la enviamos directamente al correo tras cada compra.",
    },
    {
      icon: (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
          <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
          <circle cx="9" cy="7" r="4" />
          <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
          <path d="M16 3.13a4 4 0 0 1 0 7.75" />
        </svg>
      ),
      titulo: "Roles y permisos",
      desc: "Administradores, cajeros, repartidores y clientes tienen su propio espacio seguro dentro de la plataforma.",
    },
  ];

  return (
    <>
      <Navbar />

      <main className="contactos-main">

        {/* ══════════ HERO ABOUT ══════════ */}
        <section className="about-hero">
          <div className="about-hero__content">
            <h1 className="about-hero__title">
              Conoce <span className="highlight">FarmaGO</span>
            </h1>
            <div className="about-hero__divider" />
            <p className="about-hero__desc">
              Somos una plataforma digital integral pensada para hacer tu experiencia con la farmacia
              más fácil, rápida y segura. Unificamos ventas presenciales y en línea, control de
              inventario, logística y atención al cliente en un solo lugar.
            </p>
            <p className="about-hero__desc">
              Nuestro compromiso es claro: cada producto que comprás tiene trazabilidad completa
              desde que ingresa a nuestro sistema hasta que llega a tus manos.
            </p>
          </div>

          <div className="about-hero__logo">
            <div className="about-hero__logo-glow" />
            <Image
              src="/logo.png"
              alt="FarmaGO"
              width={380}
              height={380}
              priority
              style={{ objectFit: "contain", position: "relative", zIndex: 1 }}
            />
          </div>
        </section>

        {/* ══════════ QUÉ HACEMOS ══════════ */}
        <section className="what-section">
          <div className="what-section__inner">
            <h2 className="section-title">¿Qué hace FarmaGO por ti?</h2>
            <p className="section-subtitle">
              Una farmacia moderna necesita más que un mostrador. FarmaGO integra todo lo que
              necesitas en una sola plataforma.
            </p>
            <div className="objetivos-grid">
              {objetivos.map((obj, i) => (
                <div className="objetivo-card" key={i}>
                  <div className="objetivo-card__icon">{obj.icon}</div>
                  <h3 className="objetivo-card__titulo">{obj.titulo}</h3>
                  <p className="objetivo-card__desc">{obj.desc}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

      </main>
    </>
  );
}
