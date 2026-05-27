export const getCart = () => {
  if (typeof window === "undefined") return [];

  try {
    const cart = localStorage.getItem("cart");
    return cart ? JSON.parse(cart) : [];
  } catch (error) {
    console.error("Error leyendo carrito:", error);
    return [];
  }
};

export const saveCart = (cart) => {
  localStorage.setItem("cart", JSON.stringify(cart));
};

export const addToCart = (product) => {
  const cart = getCart();

  const existingProduct = cart.find(
    (item) => item.id === product.id
  );

  if (existingProduct) {

    if (existingProduct.quantity < existingProduct.stock) {
      existingProduct.quantity += 1;
    }

  } else {

    cart.push({
      id: product.id,
      nombre: product.nombre,
      precio: product.precio,
      imagen: product.imagen,
      stock: product.stock,
      quantity: 1,
    });

  }

  saveCart(cart);
};

export const removeFromCart = (id) => {
  const updatedCart = getCart().filter(
    (item) => item.id !== id
  );

  saveCart(updatedCart);
};

export const updateQuantity = (id, quantity) => {

  const updatedCart = getCart().map((item) => {

    if (item.id === id) {

      return {
        ...item,
        quantity: Math.min(item.stock, Math.max(1, quantity)),
      };

    }

    return item;
  });

  saveCart(updatedCart);
};

export const clearCart = () => {
  localStorage.removeItem("cart");
};

export const getCartTotal = () => {
  const cart = getCart();

  return cart.reduce(
    (total, item) =>
      total + item.precio * item.quantity,
    0
  );
};
