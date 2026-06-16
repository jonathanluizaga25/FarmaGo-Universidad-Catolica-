const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function abrirCaja(cajeroId, turno) {
  const res = await fetch(`${API_URL}/caja/abrir?cajeroId=${cajeroId}&turno=${turno}`, {
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

export async function getCajasByCajero(cajeroId) {
  const res = await fetch(`${API_URL}/caja/cajero/${cajeroId}`);
  if (!res.ok) throw new Error('Error al obtener las cajas del cajero');
  return res.json();
}

export async function getCajaActiva(cajeroId) {
  const cajas = await getCajasByCajero(cajeroId);
  return cajas.find((c) => c.cerrado === false) || null;
}

export async function getCajaById(id) {
  const res = await fetch(`${API_URL}/caja/${id}`);
  if (!res.ok) throw new Error('Caja no encontrada');
  return res.json();
}

//HU-C2 - Registrar pagos
export async function registrarPago(cajaId, montoEfectivo, montoQr) {
  const res = await fetch(`${API_URL}/caja/${cajaId}/pago?montoEfectivo=${montoEfectivo}&montoQr=${montoQr}`, {
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