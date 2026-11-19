package app;

import clasesProducto.HeladoPorPeso;
import clasesProducto.Producto;
import clasesProducto.ProductoEnvasado;
import clasesProducto.Sabor;
import clasesUsuario.Administrador;
import clasesUsuario.Cliente;
import clasesVenta.DetalleVenta;
import clasesVenta.Venta;
import clasesVenta.VentaADomicilio;
import enums.EstadoEnvio;
import enums.PesoHelado;
import enums.TipoProducto;
import excepciones.PrecioInvalidoException;
import excepciones.StockInsuficienteException;
import excepciones.VentaInvalidaException;
import infraestructura.ClienteDAO;
import infraestructura.ProductoDAO;
import infraestructura.SaborDAO;
import infraestructura.VentaDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static ProductoDAO productoRepo;
    private static SaborDAO saborRepo;
    private static ClienteDAO clienteRepo;
    private static VentaDAO ventaRepo;

    public static void main(String[] args) {
        Administrador admin = null;
        try {
            productoRepo = new ProductoDAO();
            saborRepo = new SaborDAO();
            clienteRepo = new ClienteDAO();
            ventaRepo = new VentaDAO();

            int opcion = -1;
            do {

                mostrarMenuPrincipal();

                try {
                    System.out.print("Seleccione una opción: ");
                    opcion = sc.nextInt();
                    sc.nextLine();


                    switch (opcion) {
                        case 1:
                            altaProducto();
                            break;
                        case 2:
                            listarProductosOrdenados();
                            break;
                        case 3:
                            altaSabor();
                            break;
                        case 4:
                            listarSabores();
                            break;
                        case 5:
                            registrarCliente();
                            break;
                        case 6:
                            registrarVenta();
                            break;
                        case 7:
                            listarVentas();
                            break;
                        case 8:
                            admin = verificarOCrearAdmin(admin);
                            admin = menuAdministrador(admin);
                            break;
                        case 9:
                            verDetalleVenta();
                            break;
                        case 10:
                            admin = verificarOCrearAdmin(admin);
                            cambiarEstadoEnvio(admin);
                            break;
                        case 0:
                            System.out.println("Cerrando sistema...");
                            break;
                        default:
                            System.out.println("Opción inválida.");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\n[ERROR DE INGRESO]: Debe ingresar un número válido.");
                    sc.nextLine(); // Descarta lo que escribió mal el usuario
                } catch (PrecioInvalidoException | StockInsuficienteException | VentaInvalidaException e) {
                    System.out.println("\n[ERROR DE NEGOCIO]: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("\n[DATO INVÁLIDO]: " + e.getMessage());
                } catch (SQLException e) {
                    System.out.println("\n[ERROR DE BASE DE DATOS]: " + e.getMessage());
                }

            } while (opcion != 0);

        } catch (SQLException e) {
            System.out.println("No se pudo conectar a MySQL: " + e.getMessage());
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("=========================================");
        System.out.println("          GESTOR DE HELADERÍA            ");
        System.out.println("=========================================");
        System.out.println("1. Alta de Producto");
        System.out.println("2. Listar Catálogo (ordenado por precio)");
        System.out.println("3. Agregar Sabor");
        System.out.println("4. Listar Sabores");
        System.out.println("5. Registrar Cliente");
        System.out.println("6. Nueva Venta (Mostrador / A Domicilio)");
        System.out.println("7. Historial de Ventas");
        System.out.println("8. Panel Administrador");
        System.out.println("9. Ver detalle de una venta");
        System.out.println("10. Cambiar estado de un envío");
        System.out.println("0. Salir");
    }

    private static Administrador verificarOCrearAdmin(Administrador admin) {
        if (admin == null) {
            System.out.println("--- CREAR ADMINISTRADOR ---");
            System.out.println("No hay un administrador registrado. Complete los datos:");
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Cree una clave: ");
            String clave = sc.nextLine();

            admin = new Administrador(1, nombre, email, clave);
            System.out.println("Administrador creado con éxito.\n");
        }
        return admin;
    }

    private static void altaProducto() throws SQLException {
        System.out.println("--- ALTA DE PRODUCTO ---");
        System.out.print("1. Helado por Peso | 2. Producto Envasado: ");
        int tipo = sc.nextInt();
        sc.nextLine();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Stock inicial: ");
        int stock = sc.nextInt();
        sc.nextLine();

        Producto nuevo;
        if (tipo == 1) {
            System.out.print("Precio: ");
            double precio = sc.nextDouble();
            sc.nextLine();

            System.out.print("Presentación (1: CUARTO, 2: MEDIO, 3: KILO): ");
            int opPeso = sc.nextInt();
            sc.nextLine();

            PesoHelado peso;
            if (opPeso == 1) {
                peso = PesoHelado.CUARTO;
            } else if (opPeso == 2) {
                peso = PesoHelado.MEDIO;
            } else {
                peso = PesoHelado.KILO;
            }
            nuevo = new HeladoPorPeso(nombre, precio, stock, peso);
        } else {
            System.out.print("Tipo (1: PALITO_BOMBON, 2: PALITO_AGUA, 3: ESCOCES, 4: SUIZO): ");
            int opTipo = sc.nextInt();
            sc.nextLine();

            TipoProducto tp;
            if (opTipo == 1) {
                tp = TipoProducto.PALITO_BOMBON;
            } else if (opTipo == 2) {
                tp = TipoProducto.PALITO_AGUA;
            } else if (opTipo == 3) {
                tp = TipoProducto.ESCOCES;
            } else {
                tp = TipoProducto.SUIZO;
            }
            nuevo = new ProductoEnvasado(nombre, tp.getPrecio(), stock, tp);
        }

        productoRepo.guardar(nuevo);
        System.out.println("Producto guardado con ID: " + nuevo.getId());
    }

    private static void listarProductosOrdenados() throws SQLException {
        List<Producto> lista = productoRepo.listarTodos();
        Collections.sort(lista);
        System.out.println("--- CATÁLOGO ORDENADO POR PRECIO ---");
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private static void altaSabor() throws SQLException {
        System.out.println("--- NUEVO SABOR ---");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Descripción: ");
        String desc = sc.nextLine();

        Sabor s = new Sabor(nombre, desc);
        saborRepo.guardar(s);
        System.out.println("Sabor guardado con ID: " + s.getId());
    }

    private static void listarSabores() throws SQLException {
        System.out.println("--- SABORES ---");
        List<Sabor> lista = saborRepo.listarTodos();
        for (Sabor s : lista) {
            System.out.println(s);
        }
    }

    private static void registrarCliente() throws SQLException {
        System.out.println("--- REGISTRAR CLIENTE ---");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Teléfono: ");
        String tel = sc.nextLine();
        System.out.print("Dirección: ");
        String dir = sc.nextLine();

        Cliente c = new Cliente(nombre, email, tel, dir);
        clienteRepo.guardar(c);
        System.out.println("Cliente guardado con ID: " + c.getId());
    }

    private static void registrarVenta() throws SQLException, StockInsuficienteException, VentaInvalidaException {
        System.out.println("--- NUEVA VENTA ---");
        System.out.print("ID de Cliente (0 si no está registrado): ");
        int idCliente = sc.nextInt();
        sc.nextLine();

        Cliente cliente = null;
        if (idCliente > 0) {
            cliente = clienteRepo.buscarPorId(idCliente);
        }

        System.out.print("Método de pago (1: Efectivo | 2: Transferencia | 3: MP): ");
        int opPago = sc.nextInt();
        sc.nextLine();

        String pago;
        if (opPago == 1) {
            pago = "Efectivo";
        } else if (opPago == 2) {
            pago = "Transferencia";
        } else if (opPago == 3) {
            pago = "MP";
        } else {
            throw new VentaInvalidaException("Método de pago inválido.");
        }

        System.out.print("¿Es envío a domicilio? (s/n): ");
        String respEnvio = sc.nextLine();

        Venta venta;
        if (respEnvio.equalsIgnoreCase("s")) {
            System.out.print("Dirección de entrega (Enter para usar la del cliente): ");
            String dir = sc.nextLine();
            if (dir.isBlank() && cliente != null) {
                dir = cliente.getDireccion();
            }
            venta = new VentaADomicilio(pago, cliente, dir);
        } else {
            venta = new Venta(pago, cliente);
        }

        String seguir;
        do {
            listarProductosOrdenados();
            System.out.print("ID del Producto a agregar: ");
            int idProd = sc.nextInt();
            sc.nextLine();

            Producto prod = productoRepo.buscarPorId(idProd);
            if (prod == null) {
                throw new VentaInvalidaException("Producto no encontrado.");
            }

            System.out.print("Cantidad: ");
            double cant = sc.nextDouble();
            sc.nextLine();

            List<Sabor> sabores = new ArrayList<>();
            if (prod instanceof HeladoPorPeso) {
                listarSabores();
                String masSabores;
                do {
                    System.out.print("ID del sabor a elegir (0 para terminar sabores): ");
                    int idSabor = sc.nextInt();
                    sc.nextLine();

                    if (idSabor > 0) {
                        Sabor sab = saborRepo.buscarPorId(idSabor);
                        if (sab != null && sab.isDisponible()) {
                            sabores.add(sab);
                            System.out.println("Agregado: " + sab.getNombre());
                        } else {
                            System.out.println("Sabor no encontrado o sin stock.");
                        }
                    }
                    System.out.print("¿Agregar otro sabor? (s/n): ");
                    masSabores = sc.nextLine();
                } while (masSabores.equalsIgnoreCase("s"));
            }

            venta.agregarDetalle(prod, cant, sabores);

            System.out.print("¿Agregar otro producto a la venta? (s/n): ");
            seguir = sc.nextLine();
        } while (seguir.equalsIgnoreCase("s"));

        venta.confirmarVenta();
        ventaRepo.guardar(venta);

        for (DetalleVenta d : venta.getDetalles()) {
            productoRepo.actualizarStockYPrecio(d.getProducto());
        }

        System.out.println("\n¡Venta confirmada con éxito!");
        System.out.println(venta);
    }

    private static void listarVentas() throws SQLException {
        System.out.println("--- VENTAS REGISTRADAS ---");
        List<Venta> lista = ventaRepo.listarTodos();
        for (Venta v : lista) {
            System.out.println(v);
        }
    }

    private static Administrador menuAdministrador(Administrador admin) throws SQLException, PrecioInvalidoException {
        System.out.print("Clave de administrador: ");
        String clave = sc.nextLine();
        if (!admin.validarClave(clave)) {
            System.out.println("Clave incorrecta.");
            return admin;
        }

        System.out.println("--- PANEL ADMINISTRADOR ---");
        System.out.println("1. Modificar precio de un producto");
        System.out.println("2. Listar clientes registrados");
        System.out.println("3. Eliminar admin actual");
        System.out.println("4. Eliminar producto ");
        System.out.print("Seleccione una opción: ");
        int op = sc.nextInt();
        sc.nextLine();

        if (op == 1) {
            listarProductosOrdenados();
            System.out.print("ID del producto a modificar: ");
            int idProd = sc.nextInt();
            sc.nextLine();

            Producto p = productoRepo.buscarPorId(idProd);
            if (p == null) {
                System.out.println("Producto no encontrado.");
                return admin;
            }

            System.out.print("Nuevo precio: ");
            double nuevoPrecio = sc.nextDouble();
            sc.nextLine();

            admin.actualizarPrecio(p, nuevoPrecio);
            productoRepo.actualizarStockYPrecio(p);
            System.out.println("Precio actualizado en BD.");

        } else if (op == 2) {
            System.out.println("--- CLIENTES REGISTRADOS ---");
            List<Cliente> clientes = clienteRepo.listarTodos();
            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
            } else {
                for (Cliente c : clientes) {
                    System.out.println(c);
                }
            }
        } else if (op == 3) {
            System.out.print("¿Seguro que desea eliminar al administrador actual? (s/n): ");
            String conf = sc.nextLine();
            if (conf.equalsIgnoreCase("s")) {
                System.out.println("Administrador eliminado.");
                return null;
            }
        } else if (op == 4) {
            eliminarProducto();
         }
        else {
            System.out.println("Opción inválida.");
        }
        return admin;
    }


    private static void verDetalleVenta() throws SQLException {
        System.out.print("ID de la venta: ");
        int id = sc.nextInt();
        sc.nextLine();

        Venta v = ventaRepo.buscarConDetalles(id);
        if (v == null) {
            System.out.println("No existe una venta con ese ID.");
            return;
        }

        System.out.println("--- DETALLE DE LA VENTA #" + v.getId() + " ---");
        System.out.println("Fecha: " + v.getFecha().toLocalDate() + " | Pago: " + v.getMetodoPago());
        if (v.getCliente() != null) {
            System.out.println("Cliente: " + v.getCliente().getNombre());
        } else {
            System.out.println("Cliente: venta de mostrador");
        }

        for (DetalleVenta d : v.getDetalles()) {
            String linea = "- " + d.getCantidad() + " x " + d.getProducto().getNombre()
                    + " ($" + d.getPrecioUnitario() + " c/u)";
            if (!d.getSabores().isEmpty()) {
                linea += " | Sabores: ";
                for (Sabor s : d.getSabores()) {
                    linea += s.getNombre() + " ";
                }
            }
            System.out.println(linea);
        }

        if (v instanceof VentaADomicilio) {
            VentaADomicilio vd = (VentaADomicilio) v;
            System.out.println("Envío a: " + vd.getDireccionEntrega() + " | Costo: $" + vd.getCostoEnvio()
                    + " | Estado: " + vd.getEstado());
        }
        System.out.println("TOTAL: $" + v.getTotal());
    }

    private static void cambiarEstadoEnvio(Administrador admin) throws SQLException, VentaInvalidaException {
        System.out.print("Clave de administrador: ");
        String clave = sc.nextLine();
        if (!admin.validarClave(clave)) {
            System.out.println("Clave incorrecta.");
            return;
        }

        System.out.print("ID de la venta a domicilio: ");
        int id = sc.nextInt();
        sc.nextLine();

        Venta v = ventaRepo.buscarConDetalles(id);
        if (!(v instanceof VentaADomicilio)) {
            System.out.println("No existe una venta a domicilio con ese ID.");
            return;
        }
        VentaADomicilio vd = (VentaADomicilio) v;

        System.out.println("Estado actual: " + vd.getEstado());
        EstadoEnvio[] estados = EstadoEnvio.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.println((i + 1) + ". " + estados[i]);
        }
        System.out.print("Nuevo estado: ");
        int op = sc.nextInt();
        sc.nextLine();
        if (op < 1 || op > estados.length) {
            System.out.println("Opción inválida.");
            return;
        }

        admin.cambiarEstadoEnvio(vd, estados[op - 1]);
        ventaRepo.actualizarEstadoEnvio(vd);
        System.out.println("Estado actualizado a " + vd.getEstado());
        if (vd.getEstado() == EstadoEnvio.CANCELADO) {
            System.out.println("Se devolvieron los productos al stock.");
        }
    }
    private static void eliminarProducto() throws SQLException {
        System.out.println("--- ELIMINAR PRODUCTO ---");
        listarProductosOrdenados();
        System.out.print("Ingrese el ID del producto a eliminar: ");
        int idProd = sc.nextInt();
        sc.nextLine();

        Producto prod = productoRepo.buscarPorId(idProd);
        if (prod == null) {
            System.out.println("No se encontró un producto con ese ID.");
            return;
        }

        System.out.print("¿Seguro que desea eliminar '" + prod.getNombre() + "'? (s/n): ");
        String confirmacion = sc.nextLine();
        if (confirmacion.equalsIgnoreCase("s")) {
            boolean eliminado = productoRepo.eliminar(idProd);
            if (eliminado) {
                System.out.println("Producto eliminado con éxito.");
            } else {
                System.out.println("No se pudo eliminar el producto.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

}