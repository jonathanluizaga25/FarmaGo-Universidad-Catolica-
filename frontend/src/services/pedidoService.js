const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function crearPedido(datos) {
  const res = await fetch(`${API_URL}/pedidos`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(datos),
  });
  if (!res.ok) throw new Error('Error al crear pedido');
  return res.json();
}

export async function getPedidosByCliente(clienteId) {
  const res = await fetch(`${API_URL}/pedidos/cliente/${clienteId}`);
  if (!res.ok) throw new Error('Error al obtener pedidos');
  return res.json();
}

export async function getPedidoById(id) {
  const res = await fetch(`${API_URL}/pedidos/${id}`);
  if (!res.ok) throw new Error('Pedido no encontrado');
  return res.json();
}
