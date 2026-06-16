'use client';

// ─── catalogo/page.js — Catálogo de productos OTC ────────────────────────────
// Muestra los productos sin receta (OTC = Over The Counter) disponibles.
// Es la página principal para clientes.
//
// Carga en paralelo:
//   GET /api/productos/otc       → solo productos OTC con stock disponible
//   GET /api/descuentos/vigentes → descuentos activos para mostrar el precio con descuento
//
// Ambas son rutas públicas (no requieren JWT).
// ProductCard recibe el producto y la lista de descuentos para calcular el precio final.

import { useEffect, useState } from 'react';
import { API_URL } from '@/config';
import "./page.css";
import Navbar from "../../../components/Navbar/Navbar";
import ProductCard from "../../../components/ProductCard/ProductCard";

export default function CatalogoPage() {
  const [productos, setProductos] = useState([]);
  const [descuentos, setDescuentos] = useState([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    // Carga productos OTC y descuentos en paralelo para mostrar precios con descuento
    Promise.all([
      fetch(`${API_URL}/productos/otc`).then((r) => r.json()),
      fetch(`${API_URL}/descuentos/vigentes`).then((r) => r.json()).catch(() => []), // si falla, usa []
    ]).then(([prods, descs]) => {
      setProductos(prods);
      setDescuentos(descs);
    }).catch(console.error)
      .finally(() => setCargando(false));
  }, []);

  return (
    <main className="catalog-container">
      <Navbar />
      <h1>Catálogo OTC</h1>

      {cargando ? (
        <p style={{ textAlign: 'center', padding: '2rem', color: '#888' }}>
          Cargando productos...
        </p>
      ) : productos.length === 0 ? (
        <p style={{ textAlign: 'center', padding: '2rem', color: '#888' }}>
          No hay productos disponibles.
        </p>
      ) : (
        <div className="products-grid">
          {productos.map((product) => (
            <ProductCard key={product.id} product={product} descuentos={descuentos} />
          ))}
        </div>
      )}
    </main>
  );
}