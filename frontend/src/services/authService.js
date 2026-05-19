import { registrarUsuario } from '@/services/authService'
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function registrarUsuario(datos) {
  const payload = {
    nombre: datos.nombre,
    email: datos.email,
    passwordHash: datos.password,
    direccion: datos.direccion,
    telefono: datos.telefono,
    rol: 'CLIENTE',
  };
  const res = await fetch(`${API_URL}/auth/registro`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error('Error al registrar usuario');
  return res.json();
}

export async function loginUsuario(email, password) {
  const res = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  });
  if (!res.ok) throw new Error('Credenciales incorrectas');
  return res.json();
}
