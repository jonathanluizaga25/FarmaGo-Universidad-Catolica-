'use client';

import { useEffect, useState } from 'react';
import "./page.css";
import Navbar from "../../../components/Navbar/Navbar";
import ProductCard from "../../../components/ProductCard/ProductCard";

export default function CatalogoPage() {
  const [productos, setProductos] = useState([]);
  const [descuentos, setDescuentos] = useState([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    const API = API_URL;

    Promise.all([
      fetch(`${API}/productos/otc`).then((r) => r.json()),
      fetch(`${API}/descuentos/vigentes`).then((r) => r.json()).catch(() => []),
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