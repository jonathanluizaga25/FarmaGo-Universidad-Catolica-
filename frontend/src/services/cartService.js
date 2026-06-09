const API = 'http://localhost:8080/api/carrito';

function getClienteId() {
  if (typeof window === 'undefined') return null;
  const u = localStorage.getItem('usuario');
  if (!u) return null;
  return JSON.parse(u).id;
}

export async function getCart() {
  const clienteId = getClienteId();
  if (!clienteId) return null;
  const res = await fetch(`${API}/${clienteId}`);
  if (!res.ok) return null;
  return res.json();
}

export async function addToCart(product) {
  const clienteId = getClienteId();
  if (!clienteId) {
    alert('Debes iniciar sesión para agregar productos al carrito.');
    return false;
  }
  const res = await fetch(
    `${API}/${clienteId}/agregar?productoId=${product.id}&cantidad=1`,
    { method: 'POST' }
  );
  return res.ok;
}

export async function updateQuantity(productoId, cantidad) {
  const clienteId = getClienteId();
  if (!clienteId) return;
  await fetch(
    `${API}/${clienteId}/modificar/${productoId}?cantidad=${cantidad}`,
    { method: 'PUT' }
  );
}

export async function removeFromCart(productoId) {
  const clienteId = getClienteId();
  if (!clienteId) return;
  await fetch(`${API}/${clienteId}/quitar/${productoId}`, {
    method: 'DELETE',
  });
}

export async function clearCart() {
  const clienteId = getClienteId();
  if (!clienteId) return;
  await fetch(`${API}/${clienteId}/vaciar`, { method: 'DELETE' });
}
