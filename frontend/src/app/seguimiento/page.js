"use client"; 
import React, { useState, useEffect } from 'react';
import { seguimientoService } from '../../services/seguimientoService';
import './page.css'; 

export default function SeguimientoPage() {
  const [pedidos, setPedidos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);

  const clienteId = 1; 

  useEffect(() => {
    const cargarPedidos = async () => {
      try {
        setCargando(true);
        const data = await seguimientoService.obtenerPedidosPorCliente(clienteId);
        setPedidos(data);
      } catch (err) {
        setError('No se pudieron cargar tus pedidos. Inténtalo más tarde.');
      } finally {
        setCargando(false);
      }
    };

    cargarPedidos();
  }, [clienteId]);

  const obtenerClaseEstado = (estado) => {
    switch (estado?.toUpperCase()) {
      case 'PENDIENTE': return 'badge-pendiente';
      case 'CONFIRMADO': return 'badge-confirmado';
      case 'EN_CAMINO': return 'badge-en-camino';
      case 'ENTREGADO': return 'badge-entregado';
      case 'CANCELADO':
      case 'FALLIDO': return 'badge-cancelado';
      default: return 'badge-pendiente';
    }
  };

  if (cargando) {
    return <div className="estado-cargando">Cargando tus pedidos...</div>;
  }

  if (error) {
    return <div className="estado-error">{error}</div>;
  }

  return (
    <div className="seguimiento-container">
      <h1 className="seguimiento-titulo">Seguimiento de mis Pedidos</h1>

      {pedidos.length === 0 ? (
        <div className="sin-pedidos">
          <p className="sin-pedidos-texto">Todavía no hiciste ningún pedido</p>
        </div>
      ) : (
        <div className="pedidos-lista">
          {pedidos.map((pedido) => (
            <div key={pedido.numero || pedido.id} className="pedido-tarjeta">
              
              <div className="pedido-info-principal">
                <div className="pedido-encabezado">
                  <span className="pedido-numero">Pedido #{pedido.numero}</span>
                  <span className={`badge-estado ${obtenerClaseEstado(pedido.estado)}`}>
                    {pedido.estado}
                  </span>
                </div>
                <span className="pedido-fecha">
                  Fecha: {new Date(pedido.fecha).toLocaleDateString()}
                </span>
                <p className="pedido-entrega">
                  Tipo de entrega: <span>{pedido.tipoEntrega}</span>
                </p>
              </div>
              
              <div className="pedido-monto">
                <span className="total-etiqueta">Total</span>
                <span className="total-valor">${pedido.total}</span>
              </div>

            </div>
          ))}
        </div>
      )}
    </div>
  );
}