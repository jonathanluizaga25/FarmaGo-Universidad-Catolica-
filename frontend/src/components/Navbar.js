'use client';
import "./Navbar.css";

export default function Navbar({ items }) {
  return (
    <nav className="navbar">
      {items.map((item, index) => (
        <a key={index} href={item.href} className="navbar-link">
          {item.label}
        </a>
      ))}
    </nav>
  );
}