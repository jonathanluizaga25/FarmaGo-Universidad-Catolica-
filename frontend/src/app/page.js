
import Navbar from "../components/Navbar";
export default function Home() {
    const navItems = [
    { label: "Inicio", href: "#" },
    { label: "Registrarse", href: "#" },
    { label: "Iniciar Sesión", href: "#" },
  ];
  return (
    <main>
      <h1>FarmaGo</h1>
                  <Navbar items={navItems} />

      <p>Sistema Digital de Gestión - Farmacia</p>
    </main>
  );
}