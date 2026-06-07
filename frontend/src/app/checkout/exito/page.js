'use client';

import { useSearchParams, useRouter } from 'next/navigation';
import { Suspense } from 'react';

function ExitoContenido() {
  const params   = useSearchParams();
  const router   = useRouter();
  const pedidoId = params.get('pedidoId');

  return (
    <div style={{
      minHeight: '100vh', background: '#f4f7f6',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      fontFamily: 'Segoe UI, Arial, sans-serif',
    }}>
      <div style={{
        background: 'white', borderRadius: '16px', padding: '3rem 2.5rem',
        textAlign: 'center', maxWidth: '440px', width: '100%',
        boxShadow: '0 8px 30px rgba(0,0,0,0.1)',
      }}>
        <div style={{ fontSize: '4rem', marginBottom: '1rem' }}>✅</div>
        <h1 style={{ color: '#0d6e6e', marginBottom: '0.5rem' }}>¡Pedido confirmado!</h1>
        <p style={{ color: '#555', fontSize: '15px', marginBottom: '0.5rem' }}>
          Tu pedido <strong>#{pedidoId}</strong> fue registrado correctamente.
        </p>
        <p style={{ color: '#888', fontSize: '13px', marginBottom: '2rem' }}>
          El repartidor se contactará contigo al número que proporcionaste.
          Podés hacer seguimiento de tu pedido en el historial.
        </p>
        <div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
          <button onClick={() => router.push('/historial')}
            style={{ background: '#0d6e6e', color: 'white', border: 'none',
              padding: '10px 20px', borderRadius: '8px', cursor: 'pointer',
              fontWeight: 700, fontSize: '14px' }}>
            Ver mis pedidos
          </button>
          <button onClick={() => router.push('/catalogo')}
            style={{ background: '#f0f0f0', color: '#555', border: 'none',
              padding: '10px 20px', borderRadius: '8px', cursor: 'pointer',
              fontWeight: 600, fontSize: '14px' }}>
            Seguir comprando
          </button>
        </div>
      </div>
    </div>
  );
}

export default function ExitoPage() {
  return (
    <Suspense>
      <ExitoContenido />
    </Suspense>
  );
}
