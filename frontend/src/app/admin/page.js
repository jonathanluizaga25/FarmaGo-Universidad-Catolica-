"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

const API_URL = "http://localhost:8080/api";

// ═══════════════════════════════════════════════════
// COMPONENTE 1: MODAL (crear / editar)
// ═══════════════════════════════════════════════════
function ModalProducto({ producto, onCerrar, onGuardado }) {
  const esNuevo = !producto?.id;

  const [form, setForm] = useState({
    nombre:      producto?.nombre      || "",
    descripcion: producto?.descripcion || "",
    precio:      producto?.precio      || "",
    categoria:   producto?.categoria   || "",
    tipo:        producto?.tipo        || "OTC",
    stockActual: producto?.stockActual ?? "",
    stockMinimo: producto?.stockMinimo ?? "",
    imagenUrl:   producto?.imagenUrl   || "",
  });

  const [guardando, setGuardando] = useState(false);
  const [error, setError]         = useState("");

  // Actualiza un campo del formulario
  const actualizar = (campo) => (e) =>
    setForm((f) => ({ ...f, [campo]: e.target.value }));

  const guardar = async () => {
    // Validación mínima
    if (!form.nombre.trim()) {
      setError("El nombre es obligatorio.");
      return;
    }
    if (!form.precio || isNaN(parseFloat(form.precio))) {
      setError("El precio es obligatorio y debe ser un número.");
      return;
    }

    setGuardando(true);
    setError("");

    try {
      const metodo = esNuevo ? "POST" : "PUT";
      const url    = esNuevo
        ? `${API_URL}/productos`
        : `${API_URL}/productos/${producto.id}`;

      const res = await fetch(url, {
        method:  metodo,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...form,
          precio:      parseFloat(form.precio),
          stockActual: parseInt(form.stockActual)  || 0,
          stockMinimo: parseInt(form.stockMinimo)  || 5,
        }),
      });

      if (!res.ok) throw new Error("Error al guardar");

      const guardado = await res.json();

      // Le avisamos al padre qué producto se guardó y si era nuevo
      onGuardado(guardado, esNuevo);
      onCerrar();
    } catch (err) {
      setError("No se pudo guardar. Revisá los datos.");
      console.error(err);
    } finally {
      setGuardando(false);
    }
  };

  return (
    // Clic en el overlay oscuro cierra el modal
    <div className="overlay" onClick={onCerrar}>

      {/* stopPropagation evita que el clic dentro del modal lo cierre */}
      <div className="modal" onClick={(e) => e.stopPropagation()}>

        <div className="modal-header">
          <h2>{esNuevo ? "Nuevo producto" : `Editar: ${producto.nombre}`}</h2>
          <button className="btn-cerrar" onClick={onCerrar}>✕</button>
        </div>

        {error && <p className="error-msg">{error}</p>}

        <div className="modal-body">

          <label className="campo">
            <span>Nombre *</span>
            <input
              value={form.nombre}
              onChange={actualizar("nombre")}
              placeholder="Ej: Paracetamol 500mg"
            />
          </label>

          <label className="campo">
            <span>Precio (Bs.) *</span>
            <input
              type="number"
              value={form.precio}
              onChange={actualizar("precio")}
              placeholder="0.00"
              min="0"
              step="0.01"
            />
          </label>

          <label className="campo campo-full">
            <span>Descripción</span>
            <textarea
              value={form.descripcion}
              onChange={actualizar("descripcion")}
              placeholder="Descripción del producto..."
              rows={3}
            />
          </label>

          <label className="campo">
            <span>Categoría</span>
            <input
              value={form.categoria}
              onChange={actualizar("categoria")}
              placeholder="Ej: Analgésico"
            />
          </label>

          <label className="campo">
            <span>Tipo</span>
            <select value={form.tipo} onChange={actualizar("tipo")}>
              <option value="OTC">OTC (sin receta)</option>
              <option value="ETICO">Ético (con receta)</option>
            </select>
          </label>

          <label className="campo">
            <span>Stock actual</span>
            <input
              type="number"
              value={form.stockActual}
              onChange={actualizar("stockActual")}
              placeholder="0"
              min="0"
            />
          </label>

          <label className="campo">
            <span>Stock mínimo</span>
            <input
              type="number"
              value={form.stockMinimo}
              onChange={actualizar("stockMinimo")}
              placeholder="5"
              min="0"
            />
          </label>

          <label className="campo campo-full">
            <span>URL de imagen</span>
            <input
              value={form.imagenUrl}
              onChange={actualizar("imagenUrl")}
              placeholder="https://ejemplo.com/imagen.jpg"
            />
          </label>

        </div>

        <div className="modal-footer">
          <button
            className="btn-cancelar"
            onClick={onCerrar}
            disabled={guardando}
          >
            Cancelar
          </button>
          <button
            className="btn-guardar"
            onClick={guardar}
            disabled={guardando}
          >
            {guardando ? "Guardando..." : esNuevo ? "Crear producto" : "Guardar cambios"}
          </button>
        </div>

      </div>
    </div>
  );
}

// ═══════════════════════════════════════════════════
// COMPONENTE 2: PÁGINA PRINCIPAL
// ═══════════════════════════════════════════════════
export default function AdminProductosPage() {
  const router = useRouter();

  // ── Verificar que el usuario es ADMINISTRADOR ──
  const [autorizado] = useState(() => {
    if (typeof window === "undefined") return false;
    const usuario = JSON.parse(localStorage.getItem("usuario") || "null");
    return usuario?.rol === "ADMINISTRADOR";
  });

  useEffect(() => {
    if (!autorizado) {
      router.replace("/login");
    }
  }, [autorizado, router]);

  // ── Estado de la lista de productos ──
  const [productos, setProductos]   = useState([]);
  const [cargando, setCargando]     = useState(true);

  // ── Estado del modal ──
  // null        = modal cerrado
  // {}          = modal abierto para CREAR
  // { ...prod } = modal abierto para EDITAR ese producto
  const [modalProducto, setModalProducto] = useState(null);

  // ── Cargar productos al montar ──
  useEffect(() => {
    if (!autorizado) return;

    fetch(`${API_URL}/productos`)
      .then((res) => res.json())
      .then((data) => setProductos(data))
      .catch((err) => console.error("Error cargando productos:", err))
      .finally(() => setCargando(false));
  }, [autorizado]);

  // ── Eliminar producto ──
  const eliminarProducto = async (id, nombre) => {
    const confirmado = window.confirm(
      `¿Eliminar "${nombre}"?\nEsta acción no se puede deshacer.`
    );
    if (!confirmado) return;

    try {
      const res = await fetch(`${API_URL}/productos/${id}`, {
        method: "DELETE",
      });

      if (!res.ok) throw new Error("Error al eliminar");

      // Sacar el producto del estado sin recargar toda la lista
      setProductos((prev) => prev.filter((p) => p.id !== id));
    } catch (err) {
      alert("No se pudo eliminar el producto.");
      console.error(err);
    }
  };

  // ── Recibir el producto guardado desde el modal ──
  const handleGuardado = (productoGuardado, esNuevo) => {
    if (esNuevo) {
      // Era un producto nuevo: agregar al inicio de la lista
      setProductos((prev) => [productoGuardado, ...prev]);
    } else {
      // Era una edición: reemplazar el producto existente
      setProductos((prev) =>
        prev.map((p) => (p.id === productoGuardado.id ? productoGuardado : p))
      );
    }
  };

  if (!autorizado) return null;

  return (
    <div className="admin-wrapper">

      {/* ── Encabezado ── */}
      
      <div className="admin-header">
        <div>
          <h1 className="admin-titulo">Panel de Administrador</h1>
          <p className="admin-subtitulo">
            Gestioná los productos que ven los clientes en el catálogo.
          </p>
        </div>
        <button
          className="btn-nuevo"
          onClick={() => setModalProducto({})}
        >
          + Nuevo producto
        </button>
      </div>

      {/* ── Tabla de productos ── */}
      {cargando ? (
        <p className="cargando">Cargando productos...</p>
      ) : productos.length === 0 ? (
        <p className="cargando">No hay productos registrados.</p>
      ) : (
        <div className="tabla-wrapper">
          <table className="tabla">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Categoría</th>
                <th>Tipo</th>
                <th>Precio</th>
                <th>Stock actual</th>
                <th>Stock mínimo</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {productos.map((p) => (
                <tr
                  key={p.id}
                  className={
                    // Marcar en rojo si el stock está por debajo del mínimo
                    p.stockActual <= (p.stockMinimo || 5)
                      ? "fila-stock-bajo"
                      : ""
                  }
                >
                  <td>{p.id}</td>
                  <td>{p.nombre}</td>
                  <td>{p.categoria || "—"}</td>
                  <td>
                    <span className={`badge ${p.tipo === "OTC" ? "badge-otc" : "badge-etico"}`}>
                      {p.tipo}
                    </span>
                  </td>
                  <td>Bs. {parseFloat(p.precio).toFixed(2)}</td>
                  <td>{p.stockActual ?? "—"}</td>
                  <td>{p.stockMinimo ?? "—"}</td>
                  <td className="col-acciones">
                    <button
                      className="btn-editar"
                      onClick={() => setModalProducto(p)}
                    >
                      Editar
                    </button>
                    <button
                      className="btn-eliminar"
                      onClick={() => eliminarProducto(p.id, p.nombre)}
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* ── Modal: solo se muestra cuando modalProducto no es null ── */}
      {modalProducto !== null && (
        <ModalProducto
          producto={modalProducto}
          onCerrar={() => setModalProducto(null)}
          onGuardado={handleGuardado}
        />
      )}

    </div>
  );
}