const API_BASE_URL = 'http://localhost:8080/api'; 

export const seguimientoService = {
  /**
   * Obtiene todos los pedidos en seguimiento de un cliente específico
   * @param {string|number} clienteId - ID del cliente logueado
   */
  obtenerPedidosPorCliente: async (clienteId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/seguimiento/cliente/${clienteId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {
        throw new Error('Error al obtener los datos de seguimiento');
      }

      return await response.json();
    } catch (error) {
      console.error("Error en seguimientoService:", error);
      throw error;
    }
  }
};