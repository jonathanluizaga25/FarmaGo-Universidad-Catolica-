"use client";

import "./page.css";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import Navbar from "../../../components/Navbar/Navbar";
import { getCart, updateQuantity, removeFromCart } from "../../services/cartService";

export default function CarritoPage() {
  const router  = useRouter();
  const [carrito, setCarrito]   = useState(null);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    const u = localStorage.getItem('usuario');
    if (!u) { router.push('/login'); return; }
    cargarCarrito();
  }, []);

  async function cargarCarrito() {
    setCargando(true);
    const data = await getCart();
    setCarrito(data);
    setCargando(false);
  }

  async function handleCantidad(productoId, nuevaCantidad) {
    if (nuevaCantidad < 1) return;
    await updateQuantity(productoId, nuevaCantidad);
    cargarCarrito();
  }

  async function handleEliminar(productoId) {
    await removeFromCart(productoId);
    cargarCarrito();
  }

  if (cargando) {
    return (
      <>
        <Navbar />
        <main className="carrito-container">
          <h1 className="carrito-title">Carrito de Compras</h1>
          <p style={{ textAlign: 'center', color: '#888', padding: '2rem' }}>Cargando carrito...</p>
        </main>
      </>
    );
  }

  const items = carrito?.items || [];
  const total = carrito?.total || 0;

  return (
    <>
    <Navbar />
    <main className="carrito-container">
      <h1 className="carrito-title">Carrito de Compras</h1>

      {items.length === 0 ? (
        <div className="empty-cart">
          <p>Tu carrito está vacío.</p>
        </div>
      ) : (
        <>
          <div className="cart-list">
            {items.map((item) => (
              <div key={item.id} className="cart-item">

                <div className="cart-info">
                  <h3>{item.nombreProducto}</h3>

                  <p className="cart-price">
                    Bs. {item.precioUnitario}
                  </p>

                  <div className="quantity-controls">
                    <button
                      className="quantity-btn"
                      onClick={() => handleCantidad(item.productoId, item.cantidad - 1)}
                    >
                      -
                    </button>
                    <span className="quantity-value">{item.cantidad}</span>
                    <button
                      className="quantity-btn"
                      onClick={() => handleCantidad(item.productoId, item.cantidad + 1)}
                    >
                      +
                    </button>
                  </div>

                  <p className="subtotal">
                    Subtotal: Bs. {item.subtotal}
                  </p>

                  <button
                    className="remove-btn"
                    onClick={() => handleEliminar(item.productoId)}
                  >
                    Eliminar
                  </button>
                </div>

              </div>
            ))}
          </div>

          <div className="cart-summary">
            <h2 className="total">Total: Bs. {total}</h2>
            <Link href="/checkout">
              <button className="checkout-btn">Confirmar Pedido →</button>
            </Link>
          </div>
        </>
      )}
    </main>
    </>
  );
}
