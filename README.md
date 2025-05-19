# 🛒 Market Inventory Project

Aplicación web desarrollada con **Spring Boot**, **Thymeleaf** y **Bootstrap** para gestionar un inventario de productos. Implementa operaciones CRUD, paginación, ordenamiento y una interfaz moderna para facilitar el manejo de los productos en una tienda o negocio.

---

## 🚀 Despliegue en producción

🔗 Accede aquí: https://marketinventoryproject-production.up.railway.app/web/productos

---

## 🧩 Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring MVC + Thymeleaf**
- **Spring Data JPA + Hibernate**
- **H2 Database (modo dev) / PostgreSQL (producción)**
- **Bootstrap 5**
- **Maven**
- **Railway (deployment)**

---

## 📦 Funcionalidades principales

- Listado paginado de productos.
- Ordenamiento ascendente/descendente por campos.
- Registro, edición y eliminación de productos.
- Validación de formularios.
- Estilos responsivos con Bootstrap.
- Controlador REST (`/productos`) y vista web (`/web/productos`).

---

## 🛠️ Instalación local

### ✅ Requisitos

- Java 17+
- Maven 3+
- IDE (IntelliJ, Eclipse, VS Code)

### ▶️ Instrucciones

```bash
# Clonar el repositorio
git clone https://github.com/TU-USUARIO/market-inventory-project.git
cd market-inventory-project

# Compilar el proyecto
mvn clean install

# Ejecutar localmente
mvn spring-boot:run
