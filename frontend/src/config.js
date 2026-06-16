// ─── Configuración central del frontend ───────────────────────────────────────
// Este archivo reemplaza los "http://localhost:8080" que estaban hardcodeados
// en cada página. Ahora todos importan API_URL desde aquí.
// Si el proyecto se despliega en producción, basta con cambiar la variable
// de entorno NEXT_PUBLIC_API_URL y todas las páginas apuntan al nuevo servidor.

export const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// ─── fetchWithAuth ────────────────────────────────────────────────────────────
// Wrapper (envoltorio) del fetch nativo del navegador.
// Hace exactamente lo mismo que fetch(), pero agrega automáticamente el token JWT
// en el header Authorization de cada petición que lo necesite.
// Uso: fetchWithAuth('/api/alertas') en vez de fetch('/api/alertas')
export function fetchWithAuth(url, options = {}) {
  // Lee el token guardado en localStorage cuando el usuario hizo login
  const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;

  // Construye los headers: Content-Type siempre, Authorization solo si hay token
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers, // permite que el llamador agregue headers extra
  };

  // Llama al fetch normal con los headers enriquecidos
  return fetch(url, { ...options, headers });
}
