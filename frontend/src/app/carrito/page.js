"use client";

import "./page.css";

import { useEffect, useState } from "react";

import {
  getCart,
  removeFromCart,
  updateQuantity,
} from "../../services/cartService";

export default function CarritoPage() {

  const [cart, setCart] = useState([]);

  useEffect(() => {
    setCart(getCart());
  }, []);

  const refreshCart = () => {
    setCart(getCart());
  };

  const total = cart.reduce(
    (acc, item) => acc + item.precio * item.quantity,
    0
  );

  return (
    <main className="carrito-container">

      <h1 className="carrito-title">
        Carrito de Compras
      </h1>

      {cart.length === 0 ? (

        <div className="empty-cart">
          <p>Tu carrito está vacío.</p>
        </div>

      ) : (

        <>
          <div className="cart-list">

            {cart.map((item) => (

              <div
                key={item.id}
                className="cart-item"
              >

                <img
                  src={item.imagen}
                  alt={item.nombre}
                  className="cart-image"
                />

                <div className="cart-info">

                  <h3>{item.nombre}</h3>

                  <p className="cart-price">
                    Bs. {item.precio}
                  </p>

                  <p className="cart-stock">
                    Stock disponible: {item.stock}
                  </p>

                  <div className="quantity-controls">

                    <button
                      className="quantity-btn"
                      onClick={() => {
                        updateQuantity(
                          item.id,
                          item.quantity - 1
                        );
                        refreshCart();
                      }}
                    >
                      -
                    </button>

                    <span className="quantity-value">
                      {item.quantity}
                    </span>

                    <button
                      className="quantity-btn"
                      onClick={() => {
                        updateQuantity(
                          item.id,
                          item.quantity + 1
                        );
                        refreshCart();
                      }}
                      disabled={item.quantity >= item.stock}
                    >
                      +
                    </button>

                  </div>

                  <p className="subtotal">
                    Subtotal:
                    {" "}
                    Bs. {item.precio * item.quantity}
                  </p>

                  <button
                    className="remove-btn"
                    onClick={() => {
                      removeFromCart(item.id);
                      refreshCart();
                    }}
                  >
                    Eliminar
                  </button>

                </div>

              </div>

            ))}

          </div>

          <div className="cart-summary">

            <h2 className="total">
              Total: Bs. {total}
            </h2>

            <button className="checkout-btn">
              Confirmar Pedido
            </button>

          </div>
        </>

      )}

    </main>
  );
}
