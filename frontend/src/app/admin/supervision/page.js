"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import {
  getPanelSupervision,
  marcarAlertaLeida,
  getRepartidores,
  asignarPedido,
} from "@/services/supervisionService";
import "./page.css";

// ═══════════════════════════════════════════════════
// COMPONENTE: MODAL ASIGNAR REPARTIDOR
// ═══════════════════════════════════════════════════
function ModalAsignar({ pedido, repartidores, onCerrar, onAsignado }) {
  const [repartidorId, setRepartidorId] = useState("");
  const [guardando, setGuardando] = useState(false);
  const [error, setError] = useState("");

  const confirmar = async () => {
    if (!repartidorId) {
      setError("Selecciona un repartidor.");
      return;
    }

    setGuardando(true);
    setError("");

    try {
      const pedidoActualizado = await asignarPedido(pedido.id, parseInt(repartidorId, 10));
      onAsignado(pedidoActualizado);
    } catch (err) {
      setError(err.message || "No se pudo asignar el pedido.");
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="overlay" onClick={onCerrar}>
      <div className="modal-asignar" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Asignar pedido #{pedido.id}</h2>
          <button className="btn-cerrar" onClick={onCerrar}>✕</button>
        </div>

        {error && <p className="error-msg">{error}</p>}

        <div className="modal-body-asignar">
          <p className="dato-pedido">
            Cliente: <strong>{pedido.cliente?.nombre || "—"}</strong>
          </p>
          <p className="dato-pedido">
            Total: <strong>Bs. {parseFloat(pedido.total).toFixed(2)}</strong>
          </p>

          <label className="campo">
            <span>Repartidor</span>
            <select
              value={repartidorId}
              onChange={(e) => setRepartidorId(e.target.value)}
            >
              <option value="">Seleccionar repartidor...</option>
              {repartidores.map((r) => (
                <option key={r.id} value={r.id}>
                  {r.nombre}
                </option>
              ))}
            </select>
          </label>

          {repartidores.length === 0 && (
            <p className="aviso-sin-repartidores">
              No hay repartidores registrados.
            </p>
          )}
        </div>

        <div className="modal-footer">
          <button className="btn-cancelar" onClick={onCerrar} disabled={guardando}>
            Cancelar
          </button>
          <button className="btn-guardar" onClick={confirmar} disabled={guardando}>
            {guardando ? "Asignando..." : "Asignar"}
          </button>
        </div>
      </div>
    </div>
  );
}

// ═══════════════════════════════════════════════════
// COMPONENTE: PÁGINA PRINCIPAL
// ═══════════════════════════════════════════════════
export default function PanelSupervisionPage() {
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

  // ── Estado del panel ──
  const [panel, setPanel] = useState(null);
  const [repartidores, setRepartidores] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState("");

  // ── Estado del modal de asignación ──
  const [pedidoAsignar, setPedidoAsignar] = useState(null);

  // ── Cargar datos al montar ──
  useEffect(() => {
    if (!autorizado) return;

    const cargarDatos = async () => {
      try {
        const [panelData, repartidoresData] = await Promise.all([
          getPanelSupervision(),
          getRepartidores().catch(() => []), // si falla, no bloquea el panel
        ]);
        setPanel(panelData);
        setRepartidores(repartidoresData);
      } catch (err) {
        console.error(err);
        setError("No se pudo cargar el panel de supervisión.");
      } finally {
        setCargando(false);
      }
    };

    cargarDatos();
  }, [autorizado]);

  // ── Marcar alerta como leída ──
  const handleMarcarLeida = async (id) => {
    try {
      await marcarAlertaLeida(id);
      setPanel((prev) => {
        const alertasNoLeidas = prev.alertasNoLeidas.filter((a) => a.id !== id);
        return {
          ...prev,
          alertasNoLeidas,
          totalAlertasNoLeidas: alertasNoLeidas.length,
        };
      });
    } catch (err) {
      alert("No se pudo marcar la alerta como leída.");
      console.error(err);
    }
  };

  // ── Cuando se asigna un pedido, lo sacamos de "pendientes" ──
  const handleAsignado = (pedidoActualizado) => {
    setPanel((prev) => {
      const pedidosPendientes = prev.pedidosPendientes.filter(
        (p) => p.id !== pedidoActualizado.id
      );
      return {
        ...prev,
        pedidosPendientes,
        totalPedidosPendientes: pedidosPendientes.length,
      };
    });
    setPedidoAsignar(null);
  };

  if (!autorizado) return null;

  return (
    <div className="supervision-wrapper">
      {/* ── Encabezado ── */}
      <div className="supervision-header">
        <div>
          <h1 className="supervision-titulo">Panel de Supervisión</h1>
          <p className="supervision-subtitulo">
            Resumen del estado del sistema para tomar decisiones rápidas.
          </p>
        </div>
      </div>

      {cargando ? (
        <p className="cargando">Cargando panel...</p>
      ) : error ? (
        <p className="error-msg">{error}</p>
      ) : (
        <>
          {/* ── 3 tarjetas de resumen ── */}
          <div className="tarjetas-resumen">
            <div className="tarjeta tarjeta-naranja">
              <span className="tarjeta-numero">{panel.totalPedidosPendientes}</span>
              <span className="tarjeta-label">Pedidos pendientes</span>
            </div>
            <div className="tarjeta tarjeta-roja">
              <span className="tarjeta-numero">{panel.totalAlertasNoLeidas}</span>
              <span className="tarjeta-label">Alertas no leídas</span>
            </div>
            <div className="tarjeta tarjeta-verde">
              <span className="tarjeta-numero">{panel.totalDescuentosVigentes}</span>
              <span className="tarjeta-label">Descuentos vigentes</span>
            </div>
          </div>

          {/* ── Lista de pedidos pendientes ── */}
          <section className="seccion">
            <h2 className="seccion-titulo">Pedidos pendientes</h2>

            {panel.pedidosPendientes.length === 0 ? (
              <p className="vacio">No hay pedidos pendientes.</p>
            ) : (
              <div className="tabla-wrapper">
                <table className="tabla">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Cliente</th>
                      <th>Total</th>
                      <th>Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {panel.pedidosPendientes.map((p) => (
                      <tr key={p.id}>
                        <td>{p.id}</td>
                        <td>{p.cliente?.nombre || "—"}</td>
                        <td>Bs. {parseFloat(p.total).toFixed(2)}</td>
                        <td className="col-acciones">
                          <button
                            className="btn-asignar"
                            onClick={() => setPedidoAsignar(p)}
                          >
                            Asignar
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </section>

          {/* ── Lista de alertas no leídas ── */}
          <section className="seccion">
            <h2 className="seccion-titulo">Alertas no leídas</h2>

            {panel.alertasNoLeidas.length === 0 ? (
              <p className="vacio">No hay alertas pendientes.</p>
            ) : (
              <ul className="lista-alertas">
                {panel.alertasNoLeidas.map((a) => (
                  <li key={a.id} className="alerta-item">
                    <div className="alerta-contenido">
                      <span className="alerta-tipo">{a.tipo}</span>
                      <p className="alerta-mensaje">{a.mensaje}</p>
                    </div>
                    <button
                      className="btn-leer"
                      onClick={() => handleMarcarLeida(a.id)}
                    >
                      Marcar leída
                    </button>
                  </li>
                ))}
              </ul>
            )}
          </section>
        </>
      )}

      {/* ── Modal de asignación ── */}
      {pedidoAsignar !== null && (
        <ModalAsignar
          pedido={pedidoAsignar}
          repartidores={repartidores}
          onCerrar={() => setPedidoAsignar(null)}
          onAsignado={handleAsignado}
        />
      )}
    </div>
  );
}