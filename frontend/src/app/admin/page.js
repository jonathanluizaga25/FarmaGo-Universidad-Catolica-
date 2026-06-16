"use client";

// ─── admin/page.js — Panel de administración de FarmaGO ──────────────────────
// Solo accesible si el usuario tiene rol === 'ADMINISTRADOR'.
// Si no tiene ese rol (o no está logueado), lo redirige a /login.
//
// Al cargar, hace 4 llamadas en paralelo con fetchWithAuth (que agrega el JWT):
//   GET /api/productos   → lista de todos los productos
//   GET /api/pedidos     → todos los pedidos
//   GET /api/alertas     → alertas de stock y vencimiento
//   GET /api/auth/usuarios → todos los usuarios registrados
//
// Secciones del panel (sidebar):
//   Productos  → tabla con filtros, crear/editar/eliminar productos
//   Pedidos    → cambiar estado (PENDIENTE → ENTREGADO | CANCELADO)
//   Alertas    → ver y marcar como leídas
//   Usuarios   → solo lectura (ver quién está registrado)
//   Lotes, Proveedores, Caja, Supervisión, Descuentos → en construcción

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import styles from "./page.module.css";
import { API_URL, fetchWithAuth } from "@/config";

const API = API_URL;

// ─── Modal Producto ───────────────────────────────────────────────────────────
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
  const [error, setError] = useState("");

  const set = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const guardar = async () => {
    if (!form.nombre.trim()) { setError("El nombre es obligatorio."); return; }
    if (!form.precio || isNaN(parseFloat(form.precio))) { setError("El precio debe ser un número."); return; }
    setGuardando(true); setError("");
    try {
      const res = await fetchWithAuth(
        esNuevo ? `${API}/productos` : `${API}/productos/${producto.id}`,
        {
          method: esNuevo ? "POST" : "PUT",
          body: JSON.stringify({
            ...form,
            precio:      parseFloat(form.precio),
            stockActual: parseInt(form.stockActual) || 0,
            stockMinimo: parseInt(form.stockMinimo) || 5,
          }),
        }
      );
      if (!res.ok) throw new Error();
      onGuardado(await res.json(), esNuevo);
      onCerrar();
    } catch { setError("No se pudo guardar. Revisá los datos."); }
    finally { setGuardando(false); }
  };

  return (
    <div className={styles.overlay} onClick={onCerrar}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <div className={styles.modalHeader}>
          <h2>{esNuevo ? "Nuevo producto" : `Editar: ${producto.nombre}`}</h2>
          <button className={styles.btnCerrar} onClick={onCerrar}>✕</button>
        </div>
        {error && <p className={styles.errorMsg}>{error}</p>}
        <div className={styles.modalGrid}>
          <label className={styles.campo}>
            <span>Nombre *</span>
            <input value={form.nombre} onChange={set("nombre")} placeholder="Ej: Paracetamol 500mg" />
          </label>
          <label className={styles.campo}>
            <span>Precio (Bs.) *</span>
            <input type="number" value={form.precio} onChange={set("precio")} placeholder="0.00" min="0" step="0.01" />
          </label>
          <label className={`${styles.campo} ${styles.campoFull}`}>
            <span>Descripción</span>
            <textarea value={form.descripcion} onChange={set("descripcion")} placeholder="Descripción del producto..." rows={3} />
          </label>
          <label className={styles.campo}>
            <span>Categoría</span>
            <input value={form.categoria} onChange={set("categoria")} placeholder="Ej: Analgésico" />
          </label>
          <label className={styles.campo}>
            <span>Tipo</span>
            <select value={form.tipo} onChange={set("tipo")}>
              <option value="OTC">OTC (sin receta)</option>
              <option value="ETICO">Ético (con receta)</option>
            </select>
          </label>
          <label className={styles.campo}>
            <span>Stock actual</span>
            <input type="number" value={form.stockActual} onChange={set("stockActual")} placeholder="0" min="0" />
          </label>
          <label className={styles.campo}>
            <span>Stock mínimo</span>
            <input type="number" value={form.stockMinimo} onChange={set("stockMinimo")} placeholder="5" min="0" />
          </label>
          <label className={`${styles.campo} ${styles.campoFull}`}>
            <span>URL de imagen</span>
            <input value={form.imagenUrl} onChange={set("imagenUrl")} placeholder="https://..." />
          </label>
        </div>
        <div className={styles.modalFooter}>
          <button className={styles.btnSecundario} onClick={onCerrar} disabled={guardando}>Cancelar</button>
          <button className={styles.btnPrimario} onClick={guardar} disabled={guardando}>
            {guardando ? "Guardando..." : esNuevo ? "Crear producto" : "Guardar cambios"}
          </button>
        </div>
      </div>
    </div>
  );
}

// ─── Iconos SVG ───────────────────────────────────────────────────────────────
const IconProductos   = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"/><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/></svg>;
const IconPedidos     = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M9 5H7a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-2"/><rect x="9" y="3" width="6" height="4" rx="2"/><path d="M9 12h6M9 16h4"/></svg>;
const IconAlertas     = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>;
const IconUsuarios    = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>;
const IconCaja        = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="1" y="4" width="22" height="16" rx="2"/><path d="M1 10h22"/></svg>;
const IconLotes       = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/></svg>;
const IconProveedores = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>;
const IconSupervision = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>;
const IconDescuentos  = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="9" cy="9" r="2"/><circle cx="15" cy="15" r="2"/><line x1="5" y1="19" x2="19" y2="5"/></svg>;
const IconSalir       = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>;

// ─── Componente principal ─────────────────────────────────────────────────────
export default function AdminPage() {
  const router = useRouter();
  const [seccion, setSeccion] = useState("productos");
  const [usuario] = useState(() => {
    if (typeof window === "undefined") return null;
    return JSON.parse(localStorage.getItem("usuario") || "null");
  });

  // Productos
  const [productos, setProductos]         = useState([]);
  const [filtroTipo, setFiltroTipo]       = useState("todos");
  const [busqueda, setBusqueda]           = useState("");
  const [modalProducto, setModalProducto] = useState(null);

  // Pedidos
  const [pedidos, setPedidos]     = useState([]);
  const [filtroPedido, setFiltroPedido] = useState("todos");

  // Alertas
  const [alertas, setAlertas] = useState([]);

  // Usuarios
  const [usuarios, setUsuarios] = useState([]);

  // Carga
  const [cargando, setCargando] = useState(true);

  // ── Auth: guarda de ruta ─────────────────────────────────────────────────
  // Si el usuario no es ADMINISTRADOR (o no hay sesión), redirige a login.
  // Se usa useState con lazy initializer (lee localStorage una sola vez al montar)
  // para evitar el error ESLint react-hooks/set-state-in-effect.
  useEffect(() => {
    if (!usuario || usuario.rol !== "ADMINISTRADOR") { router.replace("/login"); }
  }, [usuario, router]);

  // ── Carga de datos: 4 llamadas en paralelo ────────────────────────────────
  // cancelled evita setear estado si el componente se desmonta antes de que
  // terminen las llamadas (evita memory leaks y warnings de React).
  useEffect(() => {
    if (!usuario || usuario.rol !== "ADMINISTRADOR") return;
    let cancelled = false;
    async function cargarTodo() {
      const [prods, peds, ales, users] = await Promise.all([
        fetchWithAuth(`${API}/productos`).then(r => r.json()),
        fetchWithAuth(`${API}/pedidos`).then(r => r.json()),
        fetchWithAuth(`${API}/alertas`).then(r => r.json()),
        fetchWithAuth(`${API}/auth/usuarios`).then(r => r.json()),
      ]);
      if (!cancelled) {
        setProductos(prods);
        setPedidos(peds);
        setAlertas(ales);
        setUsuarios(users);
        setCargando(false);
      }
    }
    cargarTodo();
    return () => { cancelled = true; }; // cleanup: cancela si se desmonta
  }, [usuario]);

  // ── KPIs: métricas del panel (calculadas en el cliente, sin llamadas extra) ──
  const pedidosPendientes = pedidos.filter((p) => p.estado === "PENDIENTE").length;
  const alertasNoLeidas   = alertas.filter((a) => !a.leida).length;
  const totalProductos    = productos.length;
  const stockBajo         = productos.filter((p) => p.stockActual <= (p.stockMinimo || 5)).length;

  // ── Handlers Productos ────────────────────────────────────────────────────
  const eliminarProducto = async (id, nombre) => {
    if (!confirm(`Eliminar "${nombre}"?`)) return;
    const res = await fetchWithAuth(`${API}/productos/${id}`, { method: "DELETE" });
    if (res.ok) setProductos((prev) => prev.filter((p) => p.id !== id));
    else alert("No se pudo eliminar.");
  };

  const handleGuardado = (prod, esNuevo) => {
    if (esNuevo) setProductos((prev) => [prod, ...prev]);
    else setProductos((prev) => prev.map((p) => (p.id === prod.id ? prod : p)));
  };

  // ── Handler Alertas ───────────────────────────────────────────────────────
  const marcarLeida = async (id) => {
    const res = await fetchWithAuth(`${API}/alertas/${id}/leer`, { method: "PUT" });
    if (res.ok) setAlertas((prev) => prev.map((a) => a.id === id ? { ...a, leida: true } : a));
  };

  // ── Handler Pedidos ───────────────────────────────────────────────────────
  const cambiarEstadoPedido = async (id, estado) => {
    const res = await fetchWithAuth(`${API}/pedidos/${id}/estado?estado=${estado}`, { method: "PUT" });
    if (res.ok) setPedidos((prev) => prev.map((p) => p.id === id ? { ...p, estado } : p));
  };

  const salir = () => {
    localStorage.removeItem("usuario");
    router.push("/login");
  };

  // ── Filtros ───────────────────────────────────────────────────────────────
  const productosFiltrados = productos.filter((p) => {
    const matchTipo = filtroTipo === "todos" || (filtroTipo === "stock" ? p.stockActual <= (p.stockMinimo || 5) : p.tipo === filtroTipo.toUpperCase());
    const matchBusq = !busqueda || p.nombre.toLowerCase().includes(busqueda.toLowerCase()) || (p.categoria || "").toLowerCase().includes(busqueda.toLowerCase());
    return matchTipo && matchBusq;
  });

  const pedidosFiltrados = filtroPedido === "todos" ? pedidos : pedidos.filter((p) => p.estado === filtroPedido);

  if (!usuario || cargando) {
    return (
      <div className={styles.splash}>
        <div className={styles.splashLogo}>FarmaGO</div>
        <p>Cargando panel...</p>
      </div>
    );
  }

  const navItems = [
    { id: "productos",    label: "Productos",    Icon: IconProductos },
    { id: "pedidos",      label: "Pedidos",      Icon: IconPedidos },
    { id: "alertas",      label: "Alertas",      Icon: IconAlertas,   badge: alertasNoLeidas },
    { id: "usuarios",     label: "Usuarios",     Icon: IconUsuarios },
    { id: "lotes",        label: "Lotes",        Icon: IconLotes },
    { id: "proveedores",  label: "Proveedores",  Icon: IconProveedores },
    { id: "caja",         label: "Caja",         Icon: IconCaja },
    { id: "supervision",  label: "Supervisión",  Icon: IconSupervision },
    { id: "descuentos",   label: "Descuentos",   Icon: IconDescuentos },
  ];

  return (
    <div className={styles.shell}>
      {/* ── Sidebar ── */}
      <aside className={styles.sidebar}>
        <div className={styles.sidebarLogo}>
          <span className={styles.logoText}>FarmaGO</span>
          <span className={styles.logoSub}>Admin</span>
        </div>

        <nav className={styles.sidebarNav}>
          {navItems.map(({ id, label, Icon, badge }) => (
            <button
              key={id}
              className={`${styles.navItem} ${seccion === id ? styles.navItemActive : ""}`}
              onClick={() => setSeccion(id)}
            >
              <Icon />
              <span>{label}</span>
              {badge > 0 && <span className={styles.badge}>{badge}</span>}
            </button>
          ))}
        </nav>

        <div className={styles.sidebarFooter}>
          <div className={styles.avatarRow}>
            <div className={styles.avatar}>{usuario.nombre?.[0]?.toUpperCase() || "A"}</div>
            <div>
              <div className={styles.avatarNombre}>{usuario.nombre}</div>
              <div className={styles.avatarRol}>Administrador</div>
            </div>
          </div>
          <button className={styles.btnSalir} onClick={salir}>
            <IconSalir /> Salir
          </button>
        </div>
      </aside>

      {/* ── Main ── */}
      <main className={styles.main}>

        {/* KPIs */}
        <div className={styles.kpiRow}>
          <div className={`${styles.kpiCard} ${styles.kpiNavy}`}>
            <div className={styles.kpiLabel}>Pedidos pendientes</div>
            <div className={styles.kpiValue}>{pedidosPendientes}</div>
          </div>
          <div className={`${styles.kpiCard} ${styles.kpiAmber}`}>
            <div className={styles.kpiLabel}>Alertas sin leer</div>
            <div className={styles.kpiValue}>{alertasNoLeidas}</div>
          </div>
          <div className={`${styles.kpiCard} ${styles.kpiTeal}`}>
            <div className={styles.kpiLabel}>Total productos</div>
            <div className={styles.kpiValue}>{totalProductos}</div>
          </div>
          <div className={`${styles.kpiCard} ${styles.kpiSlate}`}>
            <div className={styles.kpiLabel}>Productos con stock bajo</div>
            <div className={styles.kpiValue}>{stockBajo}</div>
          </div>
        </div>

        {/* ── Sección Productos ── */}
        {seccion === "productos" && (
          <div className={styles.seccion}>
            <div className={styles.seccionHeader}>
              <h2 className={styles.seccionTitulo}>Productos</h2>
              <button className={styles.btnPrimario} onClick={() => setModalProducto({})}>
                + Nuevo producto
              </button>
            </div>

            <div className={styles.toolbar}>
              <input
                className={styles.searchInput}
                placeholder="Buscar por nombre o categoría..."
                value={busqueda}
                onChange={(e) => setBusqueda(e.target.value)}
              />
              <div className={styles.tabs}>
                {[["todos","Todos"],["OTC","OTC"],["ETICO","Éticos"],["stock","Stock bajo"]].map(([v,l]) => (
                  <button
                    key={v}
                    className={`${styles.tab} ${filtroTipo === v ? styles.tabActive : ""}`}
                    onClick={() => setFiltroTipo(v)}
                  >
                    {l}
                  </button>
                ))}
              </div>
            </div>

            <div className={styles.tablaWrapper}>
              <table className={styles.tabla}>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Tipo</th>
                    <th>Precio</th>
                    <th>Stock</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {productosFiltrados.length === 0 ? (
                    <tr><td colSpan={7} className={styles.tdVacio}>Sin resultados.</td></tr>
                  ) : productosFiltrados.map((p) => {
                    const bajo = p.stockActual <= (p.stockMinimo || 5);
                    const pct  = Math.min(100, Math.round((p.stockActual / Math.max(p.stockMinimo * 3 || 30, 1)) * 100));
                    return (
                      <tr key={p.id} className={bajo ? styles.filaBaja : ""}>
                        <td>{p.id}</td>
                        <td>{p.nombre}</td>
                        <td>{p.categoria || "—"}</td>
                        <td>
                          <span className={p.tipo === "OTC" ? styles.badgeOtc : styles.badgeEtico}>
                            {p.tipo}
                          </span>
                        </td>
                        <td>Bs. {parseFloat(p.precio).toFixed(2)}</td>
                        <td>
                          <div className={styles.stockCell}>
                            <span className={bajo ? styles.stockBajo : ""}>{p.stockActual}</span>
                            <div className={styles.stockBar}>
                              <div className={`${styles.stockFill} ${bajo ? styles.stockFillBajo : ""}`} style={{ width: `${pct}%` }} />
                            </div>
                          </div>
                        </td>
                        <td>
                          <button className={styles.btnEditar} onClick={() => setModalProducto(p)}>Editar</button>
                          <button className={styles.btnEliminar} onClick={() => eliminarProducto(p.id, p.nombre)}>Eliminar</button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* ── Sección Pedidos ── */}
        {seccion === "pedidos" && (
          <div className={styles.seccion}>
            <div className={styles.seccionHeader}>
              <h2 className={styles.seccionTitulo}>Pedidos</h2>
            </div>
            <div className={styles.tabs} style={{ marginBottom: "1rem" }}>
              {[["todos","Todos"],["PENDIENTE","Pendientes"],["ENTREGADO","Entregados"],["CANCELADO","Cancelados"]].map(([v,l]) => (
                <button key={v} className={`${styles.tab} ${filtroPedido === v ? styles.tabActive : ""}`} onClick={() => setFiltroPedido(v)}>{l}</button>
              ))}
            </div>
            <div className={styles.tablaWrapper}>
              <table className={styles.tabla}>
                <thead>
                  <tr><th>ID</th><th>Cliente</th><th>Total</th><th>Tipo entrega</th><th>Estado</th><th>Acciones</th></tr>
                </thead>
                <tbody>
                  {pedidosFiltrados.length === 0 ? (
                    <tr><td colSpan={6} className={styles.tdVacio}>Sin pedidos.</td></tr>
                  ) : pedidosFiltrados.map((p) => (
                    <tr key={p.id}>
                      <td>#{p.id}</td>
                      <td>{p.cliente?.nombre || "—"}</td>
                      <td>Bs. {parseFloat(p.total || 0).toFixed(2)}</td>
                      <td>{p.tipoEntrega || "—"}</td>
                      <td><span className={styles[`estado${p.estado}`] || styles.estadoDefault}>{p.estado}</span></td>
                      <td>
                        {p.estado === "PENDIENTE" && (
                          <>
                            <button className={styles.btnEditar} onClick={() => cambiarEstadoPedido(p.id, "ENTREGADO")}>Entregar</button>
                            <button className={styles.btnEliminar} onClick={() => cambiarEstadoPedido(p.id, "CANCELADO")}>Cancelar</button>
                          </>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* ── Sección Alertas ── */}
        {seccion === "alertas" && (
          <div className={styles.seccion}>
            <div className={styles.seccionHeader}>
              <h2 className={styles.seccionTitulo}>Alertas</h2>
              <span className={styles.kpiLabel}>{alertasNoLeidas} sin leer</span>
            </div>
            <div className={styles.alertasList}>
              {alertas.length === 0 ? (
                <p className={styles.tdVacio}>Sin alertas.</p>
              ) : alertas.map((a) => (
                <div key={a.id} className={`${styles.alertaItem} ${a.leida ? styles.alertaLeida : ""}`}>
                  <div>
                    <span className={styles.alertaTipo}>{a.tipo}</span>
                    <p className={styles.alertaMensaje}>{a.mensaje}</p>
                  </div>
                  {!a.leida && (
                    <button className={styles.btnSecundario} onClick={() => marcarLeida(a.id)}>Leer</button>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* ── Sección Usuarios ── */}
        {seccion === "usuarios" && (
          <div className={styles.seccion}>
            <div className={styles.seccionHeader}>
              <h2 className={styles.seccionTitulo}>Usuarios</h2>
              <span className={styles.kpiLabel}>{usuarios.length} registrados</span>
            </div>
            <div className={styles.tablaWrapper}>
              <table className={styles.tabla}>
                <thead>
                  <tr><th>ID</th><th>Nombre</th><th>Email</th><th>Rol</th><th>Teléfono</th></tr>
                </thead>
                <tbody>
                  {usuarios.length === 0 ? (
                    <tr><td colSpan={5} className={styles.tdVacio}>Sin usuarios.</td></tr>
                  ) : usuarios.map((u) => (
                    <tr key={u.id}>
                      <td>{u.id}</td>
                      <td>{u.nombre}</td>
                      <td>{u.email}</td>
                      <td>
                        <span className={u.rol === "ADMINISTRADOR" ? styles.badgeAdmin : styles.badgeCliente}>{u.rol}</span>
                      </td>
                      <td>{u.telefono || "—"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* ── Secciones en construcción ── */}
        {["lotes","proveedores","caja","supervision","descuentos"].includes(seccion) && (
          <div className={styles.seccion}>
            <div className={styles.seccionHeader}>
              <h2 className={styles.seccionTitulo}>{navItems.find((n) => n.id === seccion)?.label}</h2>
            </div>
            <div className={styles.enConstruccion}>
              <p>Esta sección estará disponible próximamente.</p>
            </div>
          </div>
        )}
      </main>

      {/* ── Modal Producto ── */}
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
