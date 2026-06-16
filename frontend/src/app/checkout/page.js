'use client';

// ─── checkout/page.js — Confirmación de pedido ───────────────────────────────
// Flujo del checkout:
//   1. Al montar: verifica que hay sesión (localStorage 'usuario'), si no → /login
//   2. Carga el carrito del usuario (getCart de cartService)
//   3. Si el carrito está vacío → redirige a /carrito
//   4. El usuario completa el formulario: nombre factura, NIT, dirección, teléfono,
//      tipo de entrega (DOMICILIO | PRESENCIAL) y método de pago (EFECTIVO | QR)
//   5. Si es DOMICILIO, calcula el costo de envío consultando el backend
//      (se dispara con debounce de 600ms cuando cambia la dirección)
//   6. Al confirmar: POST /api/pedidos con los datos → redirige a /checkout/exito

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
    tipoEntrega:   'DOMICILIO',   // opciones válidas: DOMICILIO | PRESENCIAL
  });

  // Guarda de sesión + carga del carrito
  // cancelled evita actualizar estado si el componente ya se desmontó
  useEffect(() => {
    const u = localStorage.getItem('usuario');
    if (!u) { router.push('/login'); return; }
    let cancelled = false;
    async function cargarCarrito() {
      const data = await getCart();
      if (cancelled) return;
      if (!data || !data.items || data.items.length === 0) {
        router.push('/carrito'); // carrito vacío → vuelve al carrito
        return;
      }
      setCarrito(data);
      setCargando(false);
    }
    cargarCarrito();
    return () => { cancelled = true; };
  }, [router]);

  // Cálculo de costo de envío con debounce (600ms) para no llamar al backend
  // en cada tecla que escribe el usuario en el campo de dirección
  useEffect(() => {
    if (!form.direccion || form.tipoEntrega === 'PRESENCIAL') return;
    const timer = setTimeout(async () => {
      const res = await fetchWithAuth(
        `${API_URL}/pedidos/costo-envio?direccion=${encodeURIComponent(form.direccion)}`
      );
      if (res.ok) {
        const costo = await res.json();
        setCostoEnvio(Number(costo));
      }
    }, 600);
    return () => clearTimeout(timer); // cancela el timer si el usuario sigue escribiendo
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
    const total = (Number(carrito.total) + costoEnvioEfectivo).toFixed(2);

    const res = await fetchWithAuth(`${API_URL}/pedidos`, {
      method: 'POST',
      body: JSON.stringify({
        cliente:     { id: u.id },
        total:       total,
        tipoEntrega: form.tipoEntrega,
        metodoPago:  form.metodoPago,
        costoEnvio:  costoEnvioEfectivo,
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

  const subtotal           = Number(carrito?.total || 0);
  // costoEnvioEfectivo: es 0 si el cliente retira en tienda o no ingresó dirección
  const costoEnvioEfectivo = (!form.direccion || form.tipoEntrega === 'PRESENCIAL') ? 0 : costoEnvio;
  const totalFinal         = subtotal + costoEnvioEfectivo;

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
                <label className={`opcion ${form.tipoEntrega === 'PRESENCIAL' ? 'opcion-activa' : ''}`}>
                  <input type="radio" value="PRESENCIAL"
                    checked={form.tipoEntrega === 'PRESENCIAL'}
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
                {costoEnvioEfectivo > 0 && (
                  <span className="hint-envio">Costo de envío calculado: Bs. {costoEnvioEfectivo}</span>
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
            <span>{costoEnvioEfectivo === 0 ? 'Gratis' : `Bs. ${costoEnvioEfectivo}`}</span>
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
