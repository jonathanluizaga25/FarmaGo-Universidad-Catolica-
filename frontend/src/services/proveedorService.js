const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function getProveedores() {
  const res = await fetch(`${API_URL}/proveedores`);
  if (!res.ok) throw new Error('Error al obtener proveedores');
  return res.json();
}

export async function crearProveedor(datos) {
  const res = await fetch(`${API_URL}/proveedores`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(datos),
  });
  if (!res.ok) throw new Error('Error al crear proveedor');
  return res.json();
}

export async function actualizarProveedor(id, datos) {
  const res = await fetch(`${API_URL}/proveedores/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(datos),
  });
  if (!res.ok) throw new Error('Error al actualizar proveedor');
  return res.json();
}

export async function eliminarProveedor(id) {
  const res = await fetch(`${API_URL}/proveedores/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Error al eliminar proveedor');
}