import "./page.css";

import Navbar from "../../../components/Navbar/Navbar";

import { otcProducts } from "../../data/otcProducts";

import ProductCard from "../../../components/ProductCard/ProductCard";

export default function CatalogoPage() {

  return (
    <main className="catalog-container">
      <Navbar />
      <h1>Catálogo OTC</h1>

      <div className="products-grid">

        {otcProducts.map((product) => (
          <ProductCard
            key={product.id}
            product={product}
          />
        ))}

      </div>

    </main>
  );
}

