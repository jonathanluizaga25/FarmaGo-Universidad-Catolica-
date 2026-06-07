'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { getCart, clearCart } from '../../services/cartService';
import './page.css';

export default function CheckoutPage() {
  const router = useRouter();

  const [carrito, setCarrito]       = useState(null);
  const [cargando, setCargando]     = useState(true);
  const [enviando, setEnviando]     = useState(false);
  const [costoEnvio, setCostoEnvio] = useState(0);
  const [error, setError]           = useState('');

  const [form, setForm] = useState({
    nombreFactura: '',
    nit:           '',
    direccion:     '',
    telefono:      '',
    metodoPago:    'EFECTIVO',
    tipoEntrega:   'DOMICILIO',
  });

  // Redirige si no está logueado
  useEffect(() => {
    const u = localStorage.getItem('usuario');
    if (!u) { router.push('/login'); return; }
    cargarCarrito();
  }, []);

  async function cargarCarrito() {
    const data = await getCart();
    if (!data || !data.items || data.items.length === 0) {
      router.push('/carrito');
      return;
    }
    setCarrito(data);
    setCargando(false);
  }

  // Calcula costo de envío cada vez que cambia la dirección
  useEffect(() => {
    if (!form.direccion || form.tipoEntrega === 'RETIRO') {
      setCostoEnvio(0);
      return;
    }
    const timer = setTimeout(async () => {
      const res = await fetch(
        `http://localhost:8080/api/pedidos/costo-envio?direccion=${encodeURIComponent(form.direccion)}`
      );
      if (res.ok) {
        const costo = await res.json();
        setCostoEnvio(Number(costo));
      }
    }, 600);
    return () => clearTimeout(timer);
  }, [form.direccion, form.tipoEntrega]);

  function campo(key, value) {
    setForm(f => ({ ...f, [key]: value }));
  }

  async function confirmarPedido() {
    if (!form.direccion.trim()) { setError('La dirección es obligatoria.'); return; }
    if (!form.telefono.trim())  { setError('El teléfono es obligatorio.'); return; }
    if (!form.nombreFactura.trim()) { setError('El nombre para la factura es obligatorio.'); return; }

    setError('');
    setEnviando(true);

    const u = JSON.parse(localStorage.getItem('usuario'));
    const total = (Number(carrito.total) + costoEnvio).toFixed(2);

    const res = await fetch('http://localhost:8080/api/pedidos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        cliente:     { id: u.id },
        total:       total,
        tipoEntrega: form.tipoEntrega,
        metodoPago:  form.metodoPago,
        costoEnvio:  costoEnvio,
      }),
    });

    if (res.ok) {
      const pedido = await res.json();
      router.push(`/checkout/exito?pedidoId=${pedido.id}`);
    } else {
      const msg = await res.text();
      setError(msg || 'Error al confirmar el pedido.');
    }

    setEnviando(false);
  }

  if (cargando) return <p className="cargando">Cargando...</p>;

  const subtotal   = Number(carrito?.total || 0);
  const totalFinal = subtotal + costoEnvio;

  return (
    <div className="checkout-pagina">

      <div className="checkout-header">
        <button onClick={() => router.push('/carrito')} className="btn-volver">← Volver al carrito</button>
        <h1 className="checkout-titulo">Confirmar Pedido</h1>
      </div>

      <div className="checkout-grid">

        {/* ── Columna izquierda: formulario ── */}
        <div className="checkout-form">

          {/* Datos de facturación */}
          <div className="seccion">
            <h2 className="seccion-titulo">🧾 Datos de facturación</h2>
            <div className="campo-grupo">
              <label className="label">Nombre completo *</label>
              <input className="input" value={form.nombreFactura}
                onChange={e => campo('nombreFactura', e.target.value)}
                placeholder="Nombre para la factura" />
            </div>
            <div className="campo-grupo">
              <label className="label">NIT / CI</label>
              <input className="input" value={form.nit}
                onChange={e => campo('nit', e.target.value)}
                placeholder="Ej: 1234567" />
            </div>
          </div>

          {/* Datos de entrega */}
          <div className="seccion">
            <h2 className="seccion-titulo">📍 Datos de entrega</h2>

            <div className="campo-grupo">
              <label className="label">Tipo de entrega</label>
              <div className="opciones-entrega">
                <label className={`opcion ${form.tipoEntrega === 'DOMICILIO' ? 'opcion-activa' : ''}`}>
                  <input type="radio" value="DOMICILIO"
                    checked={form.tipoEntrega === 'DOMICILIO'}
                    onChange={e => campo('tipoEntrega', e.target.value)} />
                  🛵 Delivery a domicilio
                </label>
                <label className={`opcion ${form.tipoEntrega === 'RETIRO' ? 'opcion-activa' : ''}`}>
                  <input type="radio" value="RETIRO"
                    checked={form.tipoEntrega === 'RETIRO'}
                    onChange={e => campo('tipoEntrega', e.target.value)} />
                  🏪 Retiro en tienda
                </label>
              </div>
            </div>

            {form.tipoEntrega === 'DOMICILIO' && (
              <div className="campo-grupo">
                <label className="label">Dirección de entrega *</label>
                <input className="input" value={form.direccion}
                  onChange={e => campo('direccion', e.target.value)}
                  placeholder="Ej: Av. Blanco Galindo km 5, zona Norte" />
                {costoEnvio > 0 && (
                  <span className="hint-envio">Costo de envío calculado: Bs. {costoEnvio}</span>
                )}
                {costoEnvio === 0 && form.direccion && (
                  <span className="hint-envio gratis">¡Envío gratis en zona Centro!</span>
                )}
              </div>
            )}

            <div className="campo-grupo">
              <label className="label">Teléfono de contacto *</label>
              <input className="input" value={form.telefono}
                onChange={e => campo('telefono', e.target.value)}
                placeholder="Ej: 72345678"
                type="tel" />
              <span className="hint">El repartidor te contactará a este número.</span>
            </div>
          </div>

          {/* Método de pago */}
          <div className="seccion">
            <h2 className="seccion-titulo">💳 Método de pago</h2>
            <div className="opciones-entrega">
              <label className={`opcion ${form.metodoPago === 'EFECTIVO' ? 'opcion-activa' : ''}`}>
                <input type="radio" value="EFECTIVO"
                  checked={form.metodoPago === 'EFECTIVO'}
                  onChange={e => campo('metodoPago', e.target.value)} />
                💵 Efectivo
              </label>
              <label className={`opcion ${form.metodoPago === 'QR' ? 'opcion-activa' : ''}`}>
                <input type="radio" value="QR"
                  checked={form.metodoPago === 'QR'}
                  onChange={e => campo('metodoPago', e.target.value)} />
                📱 Pago QR
              </label>
            </div>
          </div>

          {error && <div className="error-msg">{error}</div>}
        </div>

        {/* ── Columna derecha: resumen del pedido ── */}
        <div className="resumen">
          <h2 className="seccion-titulo">🛒 Resumen del pedido</h2>

          <div className="resumen-items">
            {carrito.items.map(item => (
              <div key={item.id} className="resumen-item">
                <div className="resumen-nombre">
                  <span className="resumen-cantidad">{item.cantidad}x</span>
                  {item.nombreProducto}
                </div>
                <span className="resumen-subtotal">Bs. {item.subtotal}</span>
              </div>
            ))}
          </div>

          <div className="resumen-linea">
            <span>Subtotal</span>
            <span>Bs. {subtotal.toFixed(2)}</span>
          </div>
          <div className="resumen-linea">
            <span>Envío</span>
            <span>{costoEnvio === 0 ? 'Gratis' : `Bs. ${costoEnvio}`}</span>
          </div>
          <div className="resumen-total">
            <span>Total</span>
            <span>Bs. {totalFinal.toFixed(2)}</span>
          </div>

          <button
            onClick={confirmarPedido}
            className="btn-confirmar"
            disabled={enviando}
          >
            {enviando ? 'Procesando...' : 'Confirmar pedido'}
          </button>
        </div>

      </div>
    </div>
  );
}
