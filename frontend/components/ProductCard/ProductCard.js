"use client";

import { useState } from "react";
import "./ProductCard.css";
import { addToCart } from "../../src/services/cartService";

export default function ProductCard({ product }) {
  const [agregando, setAgregando] = useState(false);
  const [agregado, setAgregado]   = useState(false);

  async function handleAgregar() {
    setAgregando(true);
    const ok = await addToCart(product);
    setAgregando(false);
    if (ok) {
      setAgregado(true);
      setTimeout(() => setAgregado(false), 2000);
    }
  }

  return (
    <div className="product-card">

      <img
        src={product.imagenUrl || '/placeholder.png'}
        alt={product.nombre}
        className="product-image"
      />

      <div className="product-info">
        <h3>{product.nombre}</h3>

        {product.descripcion && (
          <p className="product-description">{product.descripcion}</p>
        )}

        {product.categoria && (
          <span className="product-categoria">{product.categoria}</span>
        )}

        <p className="price">Bs. {product.precio}</p>

        <p className="stock">Disponibles: {product.stockActual}</p>
      </div>

      <button
        className="details-btn"
        onClick={handleAgregar}
        disabled={product.stockActual <= 0 || agregando}
      >
        {product.stockActual <= 0
          ? 'Sin stock'
          : agregando
          ? 'Agregando...'
          : agregado
          ? '✓ Agregado'
          : 'Agregar al carrito'}
      </button>

    </div>
  );
}
