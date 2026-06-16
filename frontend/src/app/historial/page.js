"use client";

// ─── historial/page.js — Historial de compras del cliente ────────────────────
// Muestra todos los pedidos anteriores del usuario logueado.
// Requiere sesión: lee el ID del usuario desde localStorage y llama a:
//   GET /api/historial/{usuarioId}  (requiere JWT — usa fetchWithAuth)
//
// La respuesta es un array de pedidos, cada uno con:
//   pedidoId, fecha, estado, metodoPago, productos[], total
//
// Si el usuario no está logueado → muestra mensaje de error.
// Si no tiene pedidos → muestra "No tenés pedidos todavía."

import { API_URL, fetchWithAuth } from '@/config';

import { useEffect, useState } from "react";
import Navbar from "../../../components/Navbar/Navbar";

export default function HistorialPage() {
  const [historial, setHistorial] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadHistorial = async () => {
      try {
        // Lee el usuario del localStorage (guardado al hacer login)
        const usuario = JSON.parse(localStorage.getItem("usuario"));
        if (!usuario) {
          setError("Debes iniciar sesión para ver tu historial");
          return;
        }

        // fetchWithAuth agrega el JWT automáticamente en el header Authorization
        const response = await fetchWithAuth(`${API_URL}/historial/${usuario.id}`);
        if (!response.ok) {
          throw new Error("Error al cargar el historial");
        }

        const data = await response.json();
        setHistorial(Array.isArray(data) ? data : []); // garantiza que sea array
      } catch (err) {
        setError("Error al cargar el historial");
      } finally {
        setLoading(false);
      }
    };

    loadHistorial();
  }, []);

  if (loading) return <p style={{ padding: "2rem" }}>Cargando historial...</p>;
  if (error) return <p style={{ padding: "2rem", color: "red" }}>{error}</p>;
  if (historial.length === 0) return (
    <>
      <Navbar />
      <p style={{ padding: "2rem" }}>No tenés pedidos todavía.</p>
    </>
  );

  return (
    <>
      <Navbar />
      <main style={{ padding: "2rem", maxWidth: "800px", margin: "0 auto" }}>
        <h1>Mi historial de compras</h1>
        {historial.map((pedido) => (
          <div key={pedido.pedidoId} style={{
            border: "1px solid #e5e7eb",
            borderRadius: "8px",
            padding: "1rem",
            marginTop: "1rem"
          }}>
            <p><strong>Pedido #{pedido.pedidoId}</strong></p>
            <p>Fecha: {new Date(pedido.fecha).toLocaleDateString()}</p>
            <p>Estado: {pedido.estado}</p>
            <p>Método de pago: {pedido.metodoPago}</p>
            <table style={{ width: "100%", marginTop: "0.5rem", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ borderBottom: "1px solid #e5e7eb" }}>
                  <th style={{ textAlign: "left", padding: "4px" }}>Producto</th>
                  <th style={{ textAlign: "right", padding: "4px" }}>Cantidad</th>
                  <th style={{ textAlign: "right", padding: "4px" }}>Precio</th>
                  <th style={{ textAlign: "right", padding: "4px" }}>Subtotal</th>
                </tr>
              </thead>
              <tbody>
                {(pedido.productos ?? []).map((item, i) => (
                  <tr key={i} style={{ borderBottom: "1px solid #f3f4f6" }}>
                    <td style={{ padding: "4px" }}>{item.nombreProducto}</td>
                    <td style={{ textAlign: "right", padding: "4px" }}>{item.cantidad}</td>
                    <td style={{ textAlign: "right", padding: "4px" }}>Bs. {item.precioUnitario}</td>
                    <td style={{ textAlign: "right", padding: "4px" }}>Bs. {item.subtotal}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <p style={{ textAlign: "right", marginTop: "0.5rem" }}>
              <strong>Total: Bs. {pedido.total}</strong>
            </p>
          </div>
        ))}
      </main>
    </>
  );
}