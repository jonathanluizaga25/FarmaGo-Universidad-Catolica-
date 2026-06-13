# FarmaGO - Sistema Digital Farmacia Italica
UCB San Pablo | Ingenieria de Sistemas
## Stack tecnologico
- Frontend: Next.js 16.2.6
- Backend: Spring Boot 4.0.6 + Java 26
- Base de datos: MySQL 9.6.0
## Configuracion inicial
### 1. Variables de entorno Frontend
Crear el archivo frontend/.env.local con este contenido:
NEXT_PUBLIC_API_URL=http://localhost:8080/api
### 2. Base de datos
Copiar backend/.env.example a backend/.env y completar con tu contrasena local.
mysql -u root -p menor database/farmago.sql
### 3. Correr el proyecto
Backend:
`powershell
$env:DB_PASSWORD="tu_contrasena"; cd backend; ./mvnw spring-boot:run
```

Frontend: cd frontend && npm install && npm run dev
## Repositorio
github.com/jonathanluizaga25/FarmaGo-Universidad-Catolica-