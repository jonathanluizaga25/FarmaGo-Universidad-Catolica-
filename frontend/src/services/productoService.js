const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function getProductos() {
  const res = await fetch(`${API_URL}/productos`);
  if (!res.ok) throw new Error('Error al obtener productos');
  return res.json();
}

export async function getProductoById(id) {
  const res = await fetch(`${API_URL}/productos/${id}`);
  if (!res.ok) throw new Error('Producto no encontrado');
  return res.json();
}
