# Gestor de Heladería

Trabajo Práctico Final de **Programación II** — UTN FRMDP (Tecnicatura Universitaria en Programación).
Sistema por consola para administrar una heladería: catálogo de productos, sabores, clientes y ventas
(de mostrador o a domicilio), con persistencia en MySQL mediante JDBC.

**Integrantes:** Lucas Guaras, Santiago Iglesias y Tomas Aguer.

## Requisitos

- JDK 21
- Maven
- MySQL (servidor local en el puerto 3306)

## Puesta en marcha

1. Ejecutar el script `heladeria_db.sql` en MySQL (crea la base `heladeria_db`, las tablas y datos de ejemplo).
2. Configurar el acceso a la base. Por defecto usa `root` / `1234` en `localhost:3306`.
   Se puede cambiar sin tocar el código con las variables de entorno `DB_URL`, `DB_USER` y `DB_PASS`.
3. Compilar y ejecutar la clase `app.Main` (desde IntelliJ o con `mvn compile`).

La clave del panel de administrador está definida en `Main` (opción 8 del menú).

## Funcionalidades

- Alta y listado de productos (ordenado por precio) y de sabores.
- Registro de clientes.
- Ventas de mostrador y a domicilio, con control de stock, sabores por helado y descuento por volumen
  (2 kilos o más: 25 %). El envío a domicilio cuesta un monto fijo, gratis desde cierto total.
- Historial de ventas con sus detalles.
- Panel de administrador: cambio de precios y actualización del estado de los envíos.

## Estructura del proyecto

| Paquete | Contenido |
|---|---|
| `app` | `Main`: menú por consola |
| `clasesUsuario` | `Usuario` (abstracta), `Cliente`, `Administrador` |
| `clasesProducto` | `Producto` (abstracta), `HeladoPorPeso`, `ProductoEnvasado`, `Sabor` |
| `clasesVenta` | `Venta`, `VentaADomicilio`, `DetalleVenta` |
| `interfaz` | `Identificable`, `EnvioADomicilio` |
| `enums` | `PesoHelado`, `TipoProducto`, `EstadoEnvio` |
| `excepciones` | `PrecioInvalidoException`, `StockInsuficienteException`, `VentaInvalidaException` |
| `infraestructura` | `ConexionDB`, `RepositorioJDBC<T>` (genérica) y sus repositorios |

## Conceptos de POO aplicados

- **Herencia:** `Cliente` y `Administrador` extienden `Usuario`; `HeladoPorPeso` y `ProductoEnvasado` extienden `Producto`; `VentaADomicilio` extiende `Venta`.
- **Polimorfismo:** `calcularImporte`, `getRol` y `calcularTotal` se comportan distinto según la clase.
- **Abstracción:** clases abstractas `Usuario` y `Producto`; interfaces `Identificable` y `EnvioADomicilio`.
- **Encapsulamiento:** atributos privados, getters/setters y copias defensivas de las listas.
- **Clase genérica:** `RepositorioJDBC<T extends Identificable>`.
- **Excepciones propias:** las 3 del paquete `excepciones`.
- **Colecciones:** `List` / `ArrayList` en detalles, historial y repositorios; `Comparable` para ordenar el catálogo.
- **JDBC:** persistencia de productos, sabores, clientes y ventas (con transacción).
