"use client";

import { useState } from "react";
import "./ProductCard.css";
import { addToCart } from "../../src/services/cartService";

export default function ProductCard({ product, descuentos = [] }) {
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

  // Buscar si hay un descuento vigente para este producto
  const descuento = descuentos.find((d) => d.producto?.id === product.id);
  const precioOriginal = parseFloat(product.precio);
  const precioFinal = descuento
    ? precioOriginal * (1 - parseFloat(descuento.porcentaje) / 100)
    : precioOriginal;

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

        {/* Precio con o sin descuento */}
        {descuento ? (
          <div className="precio-wrapper">
            <span className="price-original">Bs. {precioOriginal.toFixed(2)}</span>
            <span className="price price-oferta">Bs. {precioFinal.toFixed(2)}</span>
            <span className="badge-oferta">-{descuento.porcentaje}%</span>
          </div>
        ) : (
          <p className="price">Bs. {precioOriginal.toFixed(2)}</p>
        )}

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