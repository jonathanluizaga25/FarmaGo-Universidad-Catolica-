const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function getDescuentosVigentes() {
  const res = await fetch(`${API_URL}/descuentos/vigentes`);
  if (!res.ok) throw new Error('Error al obtener descuentos vigentes');
  return res.json();
}

// Lista los acuerdos comerciales activos (proveedor+producto)
// para poblar el selector del formulario de nuevo descuento
export async function getAcuerdosActivos() {
  const res = await fetch(`${API_URL}/acuerdos/activos`);
  if (!res.ok) throw new Error('Error al obtener acuerdos comerciales');
  return res.json();
}

// POST /api/descuentos/registrar?acuerdoId=X&productoId=Y
// body: { porcentaje, fechaInicio, fechaFin }
export async function crearDescuento({ acuerdoId, productoId, porcentaje, fechaInicio, fechaFin }) {
  const url = `${API_URL}/descuentos/registrar?acuerdoId=${acuerdoId}&productoId=${productoId}`;
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ porcentaje, fechaInicio, fechaFin }),
  });
  if (!res.ok) {
    const msg = await res.text().catch(() => '');
    throw new Error(msg || 'Error al crear descuento');
  }
  return res.json();
}

// PUT /api/descuentos/{id}/desactivar
export async function desactivarDescuento(id) {
  const res = await fetch(`${API_URL}/descuentos/${id}/desactivar`, {
    method: 'PUT',
  });
  if (!res.ok) throw new Error('Error al desactivar descuento');
  return res.json();
}