'use client';

import { useEffect, useState } from 'react';
import "./page.css";
import Navbar from "../../../components/Navbar/Navbar";
import ProductCard from "../../../components/ProductCard/ProductCard";

export default function CatalogoPage() {
  const [productos, setProductos] = useState([]);
  const [cargando, setCargando]   = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/productos/otc')
      .then(res => res.json())
      .then(data => {
        setProductos(data);
        setCargando(false);
      })
      .catch(() => setCargando(false));
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
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      )}
    </main>
  );
}
