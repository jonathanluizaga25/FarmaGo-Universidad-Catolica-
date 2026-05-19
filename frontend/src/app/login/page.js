'use client';

import { useState } from 'react';
import styles from './login.module.css';

export default function LoginPage() {
  const [email, setEmail]       = useState('');
  const [password, setPassword] = useState('');
  const [showPwd, setShowPwd]   = useState(false);
  const [remember, setRemember] = useState(false);
  const [error, setError]       = useState('');

  const handleLogin = () => {
    if (!email || !password) {
      setError('Se tiene que llenar todos los campos.');
      return;
    }
    // llamada al backend para el correo y registro de usuario aqui 
    // esta para el futuro, por ahora es un demo con un correo y contraseña predefinidos
    if (email !== 'demo@farmago.com' || password !== '1234') {
      setError('Correo o contraseña incorrectos. Intenta de nuevo.');
      return;
    }
    setError('');
    alert('¡Bienvenido a FarmaGO! ✓');
  };

  return (
    <div className={styles.wrapper}>
      <main className={styles.main}>
        <div className={styles.card}>

          {/* Logo*/}
          <div className={styles.cardLogo}>
           <img src="/logo.png" alt="FarmaGO Logo" className={styles.logoImg} />
          </div>
          <h1 className={styles.title}>Inicio de sesión</h1>
          <p className={styles.subtitle}>
            Accede a tu cuenta de <strong>Farma<span className={styles.gold}>GO</span></strong>
          </p>

          {/* Error */}
          {error && <div className={styles.errorMsg}>{error}</div>}

          {/* Email */}
          <div className={styles.field}>
            <label className={styles.label} htmlFor="email">Correo electrónico</label>
            <div className={styles.inputWrap}>
              <span className={styles.iLeft}>
                <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24"
                     fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                  <polyline points="22,6 12,13 2,6"/>
                </svg>
              </span>
              <input
                id="email"
                type="email"
                className={styles.input}
                placeholder="Ingresa tu correo electrónico"
                value={email}
                onChange={e => { setEmail(e.target.value); setError(''); }}
              />
            </div>
          </div>

          {/* Password */}
          <div className={styles.field}>
            <label className={styles.label} htmlFor="password">Contraseña</label>
            <div className={styles.inputWrap}>
              <span className={styles.iLeft}>
                <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24"
                     fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
              </span>
              <input
                id="password"
                type={showPwd ? 'text' : 'password'}
                className={styles.input}
                placeholder="Ingresa tu contraseña"
                value={password}
                onChange={e => { setPassword(e.target.value); setError(''); }}
              />
              <button
                type="button"
                className={styles.iRight}
                onClick={() => setShowPwd(!showPwd)}
                aria-label="Ver contraseña"
              >
                {showPwd ? (
                  <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24"
                       fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/>
                    <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/>
                    <line x1="1" y1="1" x2="23" y2="23"/>
                  </svg>
                ) : (
                  <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24"
                       fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                  </svg>
                )}
              </button>
            </div>
          </div>

          {/* boton de login*/}
          <button className={styles.btnLogin} onClick={handleLogin}>
            Iniciar sesión
            <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24"
                 fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <line x1="5" y1="12" x2="19" y2="12"/>
              <polyline points="12 5 19 12 12 19"/>
            </svg>
          </button>
          <p className={styles.registerLink}>
            ¿No tienes cuenta? <a href="#">Crear cuenta</a>
          </p>
        </div>
      </main>
    </div>
  );
}