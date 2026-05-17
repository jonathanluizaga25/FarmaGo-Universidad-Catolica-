import Navbar from "../../components/Navbar/Navbar";
import "./page.css";

export default function Home() {
  return (
    <>
      <Navbar />

      <main className="main">
        {/* ══════════ HERO ══════════ */}
        <section className="hero">
          <div className="hero__content">
            <h1 className="hero__title">
              Tu salud,<br />nuestra prioridad
            </h1>
            <div className="hero__divider" />
            <p className="hero__subtitle">
              Encuentra los mejores productos farmacéuticos,
              cuidado personal y bienestar en un solo lugar.
            </p>
            <a href="/catalogo" className="hero__btn">
              Explorar catálogo &nbsp;→
            </a>
          </div>

          {/* Logo real como imagen decorativa del hero */}
        <div className="hero__graphic">
          <img
            src="/logo.png"
            alt="FarmaGO logo"
            className="big-cross"
          />
        </div>
        </section>

        {/* ══════════ FEATURES ══════════ */}
        <section className="features">
          <div className="features__grid">
            <div className="feature-card">
              <div className="feature-card__icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
                  <rect x="1" y="3" width="15" height="13" rx="2" />
                  <path d="M16 8h4l3 5v3h-7V8z" />
                  <circle cx="5.5" cy="18.5" r="2.5" />
                  <circle cx="18.5" cy="18.5" r="2.5" />
                </svg>
              </div>
              <div>
                <h3 className="feature-card__title">Envíos rápidos</h3>
                <p className="feature-card__desc">Recibe tus productos en la puerta de tu casa.</p>
              </div>
            </div>

            <div className="feature-card">
              <div className="feature-card__icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
                  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
                </svg>
              </div>
              <div>
                <h3 className="feature-card__title">Productos 100% originales</h3>
                <p className="feature-card__desc">Garantizamos la calidad y autenticidad.</p>
              </div>
            </div>

            <div className="feature-card">
              <div className="feature-card__icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
                  <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
                </svg>
              </div>
              <div>
                <h3 className="feature-card__title">Atención personalizada</h3>
                <p className="feature-card__desc">Estamos para asesorarte en lo que necesites.</p>
              </div>
            </div>

            <div className="feature-card">
              <div className="feature-card__icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                  <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                </svg>
              </div>
              <div>
                <h3 className="feature-card__title">Compra segura</h3>
                <p className="feature-card__desc">Tus datos y pagos siempre protegidos.</p>
              </div>
            </div>
          </div>
        </section>
      </main>

      {/* ══════════ FOOTER ══════════ */}
      <footer className="footer">
        <div className="footer__inner">
          <p className="footer__logo">
            Farma<span className="footer__logo-go">GO</span>
          </p>
          <p className="footer__copy">Tu salud, nuestra prioridad</p>
        </div>

      </footer>
    </>
  );
}
