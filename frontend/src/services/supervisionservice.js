const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// junto con el detalle de pedidos pendientes y alertas no leídas.
export async function getPanelSupervision() {
  const res = await fetch(`${API_URL}/supervision/panel`);
  if (!res.ok) throw new Error('Error al obtener el panel de supervisión');
  return res.json();
}

// Marca una alerta como leída
export async function marcarAlertaLeida(id) {
  const res = await fetch(`${API_URL}/alertas/${id}/leer`, {
    method: 'PUT',
  });
  if (!res.ok) throw new Error('Error al marcar la alerta como leída');
  return res;
}

// Lista de repartidores disponibles (para el modal de "Asignar")
export async function getRepartidores() {
  const res = await fetch(`${API_URL}/usuarios?rol=REPARTIDOR`);
  if (!res.ok) throw new Error('Error al obtener repartidores');
  return res.json();
}

// Asigna un repartidor a un pedido pendiente
export async function asignarPedido(pedidoId, repartidorId) {
  const res = await fetch(`${API_URL}/pedidos/${pedidoId}/asignar`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ repartidorId }),
  });

  if (!res.ok) {
    const mensaje = await res.text().catch(() => '');
    throw new Error(mensaje || 'No se pudo asignar el pedido');
  }

  return res.json();
}