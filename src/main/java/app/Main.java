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
import enums.PesoHelado;
import enums.TipoProducto;
import excepciones.PrecioInvalidoException;
import excepciones.StockInsuficienteException;
import excepciones.VentaInvalidaException;
import infraestructura.ClienteRepositorio;
import infraestructura.ProductoRepositorio;
import infraestructura.SaborRepositorio;
import infraestructura.VentaRepositorio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static ProductoRepositorio productoRepo;
    private static SaborRepositorio saborRepo;
    private static ClienteRepositorio clienteRepo;
    private static VentaRepositorio ventaRepo;

    public static void main(String[] args) {
        Administrador admin = new Administrador(1, "Tomás", "admin@heladeria.com", "1234");

        try {
            productoRepo = new ProductoRepositorio();
            saborRepo = new SaborRepositorio();
            clienteRepo = new ClienteRepositorio();
            ventaRepo = new VentaRepositorio();

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
                            menuAdministrador(admin);
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
        System.out.println("0. Salir");
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

        System.out.print("Método de pago (Efectivo / Tarjeta / MP): ");
        String pago = sc.nextLine();

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

    private static void menuAdministrador(Administrador admin) throws SQLException, PrecioInvalidoException {
        System.out.print("Clave de administrador: ");
        String clave = sc.nextLine();
        if (!admin.validarClave(clave)) {
            System.out.println("Clave incorrecta.");
            return;
        }

        System.out.print("ID del producto a modificar: ");
        int idProd = sc.nextInt();
        sc.nextLine();

        Producto p = productoRepo.buscarPorId(idProd);
        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.print("Nuevo precio: ");
        double nuevoPrecio = sc.nextDouble();
        sc.nextLine();

        admin.actualizarPrecio(p, nuevoPrecio);
        productoRepo.actualizarStockYPrecio(p);
        System.out.println("Precio actualizado en BD.");
    }


}