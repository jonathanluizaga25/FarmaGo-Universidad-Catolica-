"use client";

import { useState } from "react";
import Link from "next/link";
import styles from "./RegistroPage.module.css";

function MedicalCrossIcon() {
  return (
    <Link href="/" className={styles.logo}>
      <img
        src="/logo.png"
        alt="FarmaGO Logo"
        className={styles.logoImage}
      />
    </Link>
  );
}

const IconUser = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
    <circle cx="12" cy="7" r="4" />
  </svg>
);

const IconMail = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <rect x="2" y="4" width="20" height="16" rx="2" />
    <path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7" />
  </svg>
);

const IconLock = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <rect x="3" y="11" width="18" height="11" rx="2" />
    <path d="M7 11V7a5 5 0 0 1 10 0v4" />
  </svg>
);

function InputField({ label, id, type, placeholder, icon, value, onChange, error }) {
  return (
    <div className={styles.inputGroup}>
      <label htmlFor={id} className={styles.label}>
        {label}
      </label>
      <div className={styles.inputWrapper}>
        <span className={styles.inputIcon}>{icon}</span>
        <input
          id={id}
          type={type}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
          className={`${styles.input} ${error ? styles.inputError : ""}`}
        />
      </div>
      {error && <p className={styles.errorText}>{error}</p>}
    </div>
  );
}

export default function RegistroPage() {
  const [form, setForm] = useState({
    nombre: "",
    correo: "",
    contrasena: "",
    confirmar: "",
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const handleChange = (field) => (e) => {
    setForm({ ...form, [field]: e.target.value });
  };

  const validate = () => {
    const newErrors = {};
    if (!form.nombre.trim()) newErrors.nombre = "El nombre es requerido";
    if (!form.correo.trim()) newErrors.correo = "El correo es requerido";
    if (form.contrasena.length < 8) newErrors.contrasena = "Mínimo 8 caracteres";
    if (form.contrasena !== form.confirmar) newErrors.confirmar = "Las contraseñas no coinciden";
    return newErrors;
  };

  const handleSubmit = async () => {
    const validation = validate();
    if (Object.keys(validation).length > 0) {
      setErrors(validation);
      return;
    }

    setLoading(true);
    try {
      const res = await fetch('http://localhost:8080/api/auth/registro', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nombre: form.nombre,
          email: form.correo,
          passwordHash: form.contrasena,
          direccion: '',
          telefono: '',
          rol: 'CLIENTE',
        }),
      });

      if (!res.ok) {
        const msg = await res.text();
        setErrors({ correo: msg });
        return;
      }

      alert('¡Cuenta creada exitosamente! Ya puedes iniciar sesión.');
      setForm({ nombre: '', correo: '', contrasena: '', confirmar: '' });
      setErrors({});
    } catch (e) {
      setErrors({ correo: 'Error al conectar con el servidor' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <h1 className={styles.title}>Registro FarmaGo</h1>

        <div className={styles.card}>
          <div className={styles.logoContainer}>
            <MedicalCrossIcon />
          </div>

          <InputField
            label="Nombre completo"
            id="nombre"
            type="text"
            placeholder="Tu nombre"
            icon={<IconUser />}
            value={form.nombre}
            onChange={handleChange("nombre")}
            error={errors.nombre}
          />

          <InputField
            label="Correo electrónico"
            id="correo"
            type="email"
            placeholder="Tu correo"
            icon={<IconMail />}
            value={form.correo}
            onChange={handleChange("correo")}
            error={errors.correo}
          />

          <InputField
            label="Contraseña"
            id="contrasena"
            type="password"
            placeholder="Tu contraseña"
            icon={<IconLock />}
            value={form.contrasena}
            onChange={handleChange("contrasena")}
            error={errors.contrasena}
          />

          <InputField
            label="Confirmar contraseña"
            id="confirmar"
            type="password"
            placeholder="Confirma contraseña"
            icon={<IconLock />}
            value={form.confirmar}
            onChange={handleChange("confirmar")}
            error={errors.confirmar}
          />

          <button
            onClick={handleSubmit}
            className={styles.submitBtn}
            disabled={loading}
          >
            {loading ? 'Creando cuenta...' : 'Crear cuenta'}
          </button>

          <p className={styles.loginText}>
            ¿Ya tienes cuenta?{" "}
            <Link href="/login" className={styles.link}>
              Inicia sesión
            </Link>
          </p>
        </div>
      </main>
    </div>
  );
}
