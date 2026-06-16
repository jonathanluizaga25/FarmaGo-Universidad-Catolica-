// ─── cajaService.js — Llamadas al backend para el módulo de Caja ─────────────
// Todas las funciones usan fetchWithAuth para enviar el JWT automáticamente.
// Los endpoints de caja requieren rol CAJERO o ADMINISTRADOR en el backend.

import { fetchWithAuth } from '@/config';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// Abre una nueva caja para el cajero en el turno indicado
export async function abrirCaja(cajeroId, turno) {
  const res = await fetchWithAuth(`${API_URL}/caja/abrir?cajeroId=${cajeroId}&turno=${turno}`, {
    method: 'POST',
  });
  if (!res.ok) {
    let mensaje = 'No se pudo abrir la caja';
    try {
      const data = await res.json();
      if (data?.message && data.message !== 'No message available') {
        mensaje = data.message;
      }
    } catch {
      // sin cuerpo JSON, se usa el mensaje por defecto
    }
    throw new Error(mensaje);
  }
  return res.json();
}

// Devuelve todas las cajas del cajero (abiertas y cerradas)
export async function getCajasByCajero(cajeroId) {
  const res = await fetchWithAuth(`${API_URL}/caja/cajero/${cajeroId}`);
  if (!res.ok) throw new Error('Error al obtener las cajas del cajero');
  return res.json();
}

// Devuelve la caja actualmente abierta del cajero, o null si no tiene ninguna
export async function getCajaActiva(cajeroId) {
  const cajas = await getCajasByCajero(cajeroId);
  return cajas.find((c) => c.cerrado === false) || null;
}

// Devuelve los datos de una caja por su ID
export async function getCajaById(id) {
  const res = await fetchWithAuth(`${API_URL}/caja/${id}`);
  if (!res.ok) throw new Error('Caja no encontrada');
  return res.json();
}

// HU-C2 — Registra un pago en la caja (suma al total efectivo y/o QR)
export async function registrarPago(cajaId, montoEfectivo, montoQr) {
  const res = await fetchWithAuth(`${API_URL}/caja/${cajaId}/pago?montoEfectivo=${montoEfectivo}&montoQr=${montoQr}`, {
    method: 'PUT',
  });
  if (!res.ok) {
    let mensaje = 'No se pudo registrar el pago';
    try {
      const data = await res.json();
      if (data?.message && data.message !== 'No message available') {
        mensaje = data.message;
      }
    } catch {
      // sin cuerpo JSON
    }
    throw new Error(mensaje);
  }
  return res.json();
}

// HU-C3 — Cierra la caja del turno actual (no se puede reabrir)
export async function cerrarCaja(cajaId) {
  const res = await fetchWithAuth(`${API_URL}/caja/${cajaId}/cerrar`, {
    method: 'PUT',
  });
  if (!res.ok) {
    let mensaje = 'No se pudo cerrar la caja';
    try {
      const data = await res.json();
      if (data?.message && data.message !== 'No message available') {
        mensaje = data.message;
      }
    } catch {
      // sin cuerpo JSON
    }
    throw new Error(mensaje);
  }
  return res.json();
}
