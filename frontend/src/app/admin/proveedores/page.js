"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import {
  getProveedores,
  crearProveedor,
  actualizarProveedor,
  eliminarProveedor,
} from "@/services/proveedorService";
import {
  getDescuentosVigentes,
  getAcuerdosActivos,
  crearDescuento,
  desactivarDescuento,
} from "@/services/descuentoService";
import "./page.css";

// ═══════════════════════════════════════════════════
// MODAL: CREAR / EDITAR PROVEEDOR
// ═══════════════════════════════════════════════════
function ModalProveedor({ proveedor, onCerrar, onGuardado }) {
  const esEdicion = Boolean(proveedor?.id);
  const [form, setForm] = useState({
    nombre: proveedor?.nombre || "",
    contacto: proveedor?.contacto || "",
    telefono: proveedor?.telefono || "",
  });
  const [guardando, setGuardando] = useState(false);
  const [error, setError] = useState("");

  const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const guardar = async () => {
    if (!form.nombre.trim() || !form.contacto.trim()) {
      setError("Nombre y contacto son obligatorios.");
      return;
    }
    setGuardando(true);
    setError("");
    try {
      const resultado = esEdicion
        ? await actualizarProveedor(proveedor.id, form)
        : await crearProveedor(form);
      onGuardado(resultado, esEdicion);
    } catch (err) {
      setError(err.message || "No se pudo guardar el proveedor.");
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="overlay" onClick={onCerrar}>
      <div className="modal-prov" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{esEdicion ? "Editar proveedor" : "Nuevo proveedor"}</h2>
          <button className="btn-cerrar" onClick={onCerrar}>✕</button>
        </div>

        {error && <p className="error-msg">{error}</p>}

        <div className="modal-body-form">
          <label className="campo">
            <span>Nombre *</span>
            <input name="nombre" value={form.nombre} onChange={cambiar} placeholder="Nombre del proveedor" />
          </label>
          <label className="campo">
            <span>Contacto *</span>
            <input name="contacto" value={form.contacto} onChange={cambiar} placeholder="Persona de contacto" />
          </label>
          <label className="campo">
            <span>Teléfono</span>
            <input name="telefono" value={form.telefono} onChange={cambiar} placeholder="Ej: 77712345" />
          </label>
        </div>

        <div className="modal-footer">
          <button className="btn-cancelar" onClick={onCerrar} disabled={guardando}>Cancelar</button>
          <button className="btn-guardar" onClick={guardar} disabled={guardando}>
            {guardando ? "Guardando..." : "Guardar"}
          </button>
        </div>
      </div>
    </div>
  );
}

// ═══════════════════════════════════════════════════
// MODAL: CREAR DESCUENTO
// ═══════════════════════════════════════════════════
function ModalDescuento({ acuerdos, onCerrar, onGuardado }) {
  const [form, setForm] = useState({
    acuerdoId: "",
    porcentaje: "",
    fechaInicio: "",
    fechaFin: "",
  });
  const [guardando, setGuardando] = useState(false);
  const [error, setError] = useState("");

  const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const guardar = async () => {
    if (!form.acuerdoId || !form.porcentaje || !form.fechaInicio || !form.fechaFin) {
      setError("Todos los campos son obligatorios.");
      return;
    }
    if (parseFloat(form.porcentaje) <= 0 || parseFloat(form.porcentaje) > 100) {
      setError("El porcentaje debe estar entre 1 y 100.");
      return;
    }
    if (form.fechaFin < form.fechaInicio) {
      setError("La fecha de fin no puede ser anterior a la de inicio.");
      return;
    }

    setGuardando(true);
    setError("");

    try {
      // Buscamos el acuerdo seleccionado para obtener productoId
      const acuerdoSeleccionado = acuerdos.find((a) => a.id === parseInt(form.acuerdoId, 10));
      const productoId = acuerdoSeleccionado?.producto?.id;

      if (!productoId) {
        setError("No se encontró el producto del acuerdo.");
        return;
      }

      const resultado = await crearDescuento({
        acuerdoId: parseInt(form.acuerdoId, 10),
        productoId,
        porcentaje: parseFloat(form.porcentaje),
        fechaInicio: form.fechaInicio,
        fechaFin: form.fechaFin,
      });
      onGuardado(resultado);
    } catch (err) {
      setError(err.message || "No se pudo crear el descuento.");
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="overlay" onClick={onCerrar}>
      <div className="modal-prov" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Nuevo descuento</h2>
          <button className="btn-cerrar" onClick={onCerrar}>✕</button>
        </div>

        {error && <p className="error-msg">{error}</p>}

        <div className="modal-body-form">
          <label className="campo campo-full">
            <span>Proveedor → Producto (Acuerdo Comercial) *</span>
            <select name="acuerdoId" value={form.acuerdoId} onChange={cambiar}>
              <option value="">Seleccionar acuerdo...</option>
              {acuerdos.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.proveedor?.nombre || "?"} → {a.producto?.nombre || "?"}
                </option>
              ))}
            </select>
            {acuerdos.length === 0 && (
              <span className="aviso-campo">No hay acuerdos comerciales activos.</span>
            )}
          </label>

          <label className="campo">
            <span>Porcentaje de descuento (%) *</span>
            <input
              name="porcentaje"
              type="number"
              min="1"
              max="100"
              value={form.porcentaje}
              onChange={cambiar}
              placeholder="Ej: 15"
            />
          </label>

          <label className="campo">
            <span>Fecha inicio *</span>
            <input name="fechaInicio" type="date" value={form.fechaInicio} onChange={cambiar} />
          </label>

          <label className="campo">
            <span>Fecha fin *</span>
            <input name="fechaFin" type="date" value={form.fechaFin} onChange={cambiar} />
          </label>
        </div>

        <div className="modal-footer">
          <button className="btn-cancelar" onClick={onCerrar} disabled={guardando}>Cancelar</button>
          <button className="btn-guardar" onClick={guardar} disabled={guardando}>
            {guardando ? "Guardando..." : "Crear descuento"}
          </button>
        </div>
      </div>
    </div>
  );
}

// ═══════════════════════════════════════════════════
// PÁGINA PRINCIPAL
// ═══════════════════════════════════════════════════
export default function ProveedoresPage() {
  const router = useRouter();

  const [autorizado] = useState(() => {
    if (typeof window === "undefined") return false;
    const usuario = JSON.parse(localStorage.getItem("usuario") || "null");
    return usuario?.rol === "ADMINISTRADOR";
  });

  useEffect(() => {
    if (!autorizado) router.replace("/login");
  }, [autorizado, router]);

  // ── Tabs ──
  const [tabActiva, setTabActiva] = useState("proveedores");

  // ── Proveedores ──
  const [proveedores, setProveedores] = useState([]);
  const [cargandoProv, setCargandoProv] = useState(true);
  const [modalProv, setModalProv] = useState(null); // null | {} | { id, nombre, ... }
  const [confirmEliminar, setConfirmEliminar] = useState(null);

  // ── Descuentos ──
  const [descuentos, setDescuentos] = useState([]);
  const [acuerdos, setAcuerdos] = useState([]);
  const [cargandoDesc, setCargandoDesc] = useState(true);
  const [modalDesc, setModalDesc] = useState(false);

  // Cargar proveedores
  useEffect(() => {
    if (!autorizado) return;
    getProveedores()
      .then(setProveedores)
      .catch(console.error)
      .finally(() => setCargandoProv(false));
  }, [autorizado]);

  // Cargar descuentos y acuerdos
  useEffect(() => {
    if (!autorizado) return;
    Promise.all([getDescuentosVigentes(), getAcuerdosActivos()])
      .then(([desc, acuerd]) => {
        setDescuentos(desc);
        setAcuerdos(acuerd);
      })
      .catch(console.error)
      .finally(() => setCargandoDesc(false));
  }, [autorizado]);

  // ── Handlers proveedores ──
  const handleGuardadoProv = (resultado, esEdicion) => {
    if (esEdicion) {
      setProveedores((prev) => prev.map((p) => (p.id === resultado.id ? resultado : p)));
    } else {
      setProveedores((prev) => [...prev, resultado]);
    }
    setModalProv(null);
  };

  const handleEliminar = async (id) => {
    try {
      await eliminarProveedor(id);
      setProveedores((prev) => prev.filter((p) => p.id !== id));
    } catch {
      alert("No se pudo eliminar el proveedor.");
    } finally {
      setConfirmEliminar(null);
    }
  };

  // ── Handlers descuentos ──
  const handleGuardadoDesc = (nuevo) => {
    setDescuentos((prev) => [...prev, nuevo]);
    setModalDesc(false);
  };

  const handleDesactivar = async (id) => {
    try {
      await desactivarDescuento(id);
      setDescuentos((prev) => prev.filter((d) => d.id !== id));
    } catch {
      alert("No se pudo desactivar el descuento.");
    }
  };

  if (!autorizado) return null;

  return (
    <div className="supervision-wrapper">
      <div className="supervision-header">
        <div>
          <h1 className="supervision-titulo">Gestión de Proveedores y Descuentos</h1>
          <p className="supervision-subtitulo">Administrá proveedores y configurá descuentos vigentes.</p>
        </div>
      </div>

      {/* ── Tabs ── */}
      <div className="tabs">
        <button
          className={`tab ${tabActiva === "proveedores" ? "tab-activa" : ""}`}
          onClick={() => setTabActiva("proveedores")}
        >
          Proveedores
        </button>
        <button
          className={`tab ${tabActiva === "descuentos" ? "tab-activa" : ""}`}
          onClick={() => setTabActiva("descuentos")}
        >
          Descuentos
        </button>
      </div>

      {/* ═══════════ TAB PROVEEDORES ═══════════ */}
      {tabActiva === "proveedores" && (
        <section className="seccion">
          <div className="seccion-header">
            <h2 className="seccion-titulo">Proveedores</h2>
            <button className="btn-nuevo" onClick={() => setModalProv({})}>
              + Nuevo proveedor
            </button>
          </div>

          {cargandoProv ? (
            <p className="cargando">Cargando proveedores...</p>
          ) : proveedores.length === 0 ? (
            <p className="vacio">No hay proveedores registrados.</p>
          ) : (
            <div className="tabla-wrapper">
              <table className="tabla">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Contacto</th>
                    <th>Teléfono</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {proveedores.map((p) => (
                    <tr key={p.id}>
                      <td>{p.id}</td>
                      <td>{p.nombre}</td>
                      <td>{p.contacto}</td>
                      <td>{p.telefono || "—"}</td>
                      <td className="col-acciones">
                        <button className="btn-editar" onClick={() => setModalProv(p)}>
                          Editar
                        </button>
                        <button className="btn-eliminar" onClick={() => setConfirmEliminar(p)}>
                          Eliminar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      )}

      {/* ═══════════ TAB DESCUENTOS ═══════════ */}
      {tabActiva === "descuentos" && (
        <section className="seccion">
          <div className="seccion-header">
            <h2 className="seccion-titulo">Descuentos vigentes</h2>
            <button className="btn-nuevo" onClick={() => setModalDesc(true)}>
              + Nuevo descuento
            </button>
          </div>

          {cargandoDesc ? (
            <p className="cargando">Cargando descuentos...</p>
          ) : descuentos.length === 0 ? (
            <p className="vacio">No hay descuentos vigentes.</p>
          ) : (
            <div className="tabla-wrapper">
              <table className="tabla">
                <thead>
                  <tr>
                    <th>Producto</th>
                    <th>Proveedor</th>
                    <th>Descuento</th>
                    <th>Desde</th>
                    <th>Hasta</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {descuentos.map((d) => (
                    <tr key={d.id}>
                      <td>{d.producto?.nombre || "—"}</td>
                      <td>{d.acuerdo?.proveedor?.nombre || "—"}</td>
                      <td>
                        <span className="badge-descuento">{d.porcentaje}%</span>
                      </td>
                      <td>{d.fechaInicio}</td>
                      <td>{d.fechaFin}</td>
                      <td className="col-acciones">
                        <button className="btn-desactivar" onClick={() => handleDesactivar(d.id)}>
                          Desactivar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      )}

      {/* ── Modales ── */}
      {modalProv !== null && (
        <ModalProveedor
          proveedor={modalProv}
          onCerrar={() => setModalProv(null)}
          onGuardado={handleGuardadoProv}
        />
      )}

      {modalDesc && (
        <ModalDescuento
          acuerdos={acuerdos}
          onCerrar={() => setModalDesc(false)}
          onGuardado={handleGuardadoDesc}
        />
      )}

      {/* ── Confirmación eliminar ── */}
      {confirmEliminar && (
        <div className="overlay" onClick={() => setConfirmEliminar(null)}>
          <div className="modal-prov modal-confirm" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Confirmar eliminación</h2>
              <button className="btn-cerrar" onClick={() => setConfirmEliminar(null)}>✕</button>
            </div>
            <p className="confirm-texto">
              ¿Seguro que querés eliminar a <strong>{confirmEliminar.nombre}</strong>? Esta acción no se puede deshacer.
            </p>
            <div className="modal-footer">
              <button className="btn-cancelar" onClick={() => setConfirmEliminar(null)}>Cancelar</button>
              <button className="btn-eliminar-confirm" onClick={() => handleEliminar(confirmEliminar.id)}>
                Sí, eliminar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}