"use client";

import "./ProductCard.css";

import { addToCart } from "../../src/services/cartService";

export default function ProductCard({ product }) {

  return (
    <div className="product-card">

      <img
        src={product.imagen}
        alt={product.nombre}
        className="product-image"
      />

      <h3>{product.nombre}</h3>

      <p className="price">
        Bs. {product.precio}
      </p>

      <p className="stock">
        Disponibles: {product.stock}
      </p>

      <button
        className="details-btn"
        onClick={() => {
  console.log("Producto agregado:", product);
  addToCart(product);
}}
      >
        Agregar al carrito
      </button>

    </div>
  );
}