package infraestructura;

import clasesProducto.Producto;
import clasesProducto.Sabor;
import clasesUsuario.Cliente;
import clasesVenta.DetalleVenta;
import clasesVenta.Venta;
import clasesVenta.VentaADomicilio;
import enums.EstadoEnvio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaRepositorio extends RepositorioJDBC<Venta> {

    private ClienteRepositorio clienteRepo;
    private ProductoRepositorio productoRepo;

    public VentaRepositorio() throws SQLException {
        super();
        this.clienteRepo = new ClienteRepositorio();
        this.productoRepo = new ProductoRepositorio();
    }

    @Override
    protected String getTabla() {
        return "ventas";
    }

    @Override
    protected String getSqlInsert() {
        return "INSERT INTO ventas (fecha,metodo_pago,total,cliente_id,es_domicilio,direccion_entrega,costo_envio,estado_envio) VALUES(?,?,?,?,?,?,?,?)";
    }

    @Override
    protected void cargarInsert(PreparedStatement ps, Venta v) throws SQLException {
        ps.setTimestamp(1, Timestamp.valueOf(v.getFecha()));
        ps.setString(2, v.getMetodoPago());
        ps.setDouble(3, v.getTotal());

        //Venta de local
        if (v.getCliente() != null && v.getCliente().getId() > 0) {
            ps.setInt(4, v.getCliente().getId());
        } else {
            ps.setNull(4, Types.INTEGER);
        }

        //A domicilio guardamos los datos del envio
        if (v instanceof VentaADomicilio vd) {
            ps.setBoolean(5, true);
            ps.setString(6, vd.getDireccionEntrega());
            ps.setDouble(7, vd.getCostoEnvio());
            ps.setString(8, vd.getEstado().name());
        } else {
            ps.setBoolean(5, false);
            ps.setNull(6, Types.VARCHAR);
            ps.setDouble(7, 0);
            ps.setNull(8, Types.VARCHAR);
        }
    }

    //Guarda la venta y sus detalles
    @Override
    public void guardar(Venta v) throws SQLException {
        if (!v.isConfirmada()) {
            throw new IllegalStateException("Solo se pueden guardar ventas confirmadas");
        }
        conexion.setAutoCommit(false);
        try {
            super.guardar(v); //Guarda la venta y le pone el id que genero la base
            guardarDetalles(v);
            conexion.commit();
        } catch (SQLException | RuntimeException e) {
            //Deshace lo que se guardo
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    private void guardarDetalles(Venta v) throws SQLException {
        String sql = "INSERT INTO detalle_ventas (venta_id,producto_id,cantidad,precio_unitario,sabores_elegidos) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (DetalleVenta d : v.getDetalles()) {
                ps.setInt(1, v.getId());
                ps.setInt(2, d.getProducto().getId());
                ps.setDouble(3, d.getCantidad());
                ps.setDouble(4, d.getPrecioUnitario());
                ps.setString(5, saboresATexto(d.getSabores()));
                ps.executeUpdate();
            }
        }
    }

    private String saboresATexto(List<Sabor> sabores) {
        if (sabores == null || sabores.isEmpty()) {
            return null;
        }
        String texto = "";
        for (Sabor s : sabores) {
            if (!texto.isEmpty()) {
                texto += ", ";
            }
            texto += s.getNombre();
        }
        return texto;
    }

    @Override
    protected Venta mapearFila(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Timestamp fecha = rs.getTimestamp("fecha");
        String metodoPago = rs.getString("metodo_pago");
        double total = rs.getDouble("total");

        int clienteId = rs.getInt("cliente_id");
        Cliente cliente = rs.wasNull() ? null : clienteRepo.buscarPorId(clienteId);

        if (rs.getBoolean("es_domicilio")) {
            return new VentaADomicilio(id, fecha.toLocalDateTime(), metodoPago, total, cliente,
                    rs.getString("direccion_entrega"),
                    rs.getDouble("costo_envio"),
                    EstadoEnvio.valueOf(rs.getString("estado_envio")));
        }
        return new Venta(id, fecha.toLocalDateTime(), metodoPago, total, cliente);
    }

    //Busca una venta por id y le carga sus productos
    public Venta buscarConDetalles(int id) throws SQLException {
        Venta v = buscarPorId(id);
        if (v == null) {
            return null;
        }
        String sql = "SELECT * FROM detalle_ventas WHERE venta_id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = productoRepo.buscarPorId(rs.getInt("producto_id"));
                    DetalleVenta d = new DetalleVenta(p, rs.getDouble("cantidad"),
                            rs.getDouble("precio_unitario"), textoASabores(rs.getString("sabores_elegidos")));
                    v.restaurarDetalle(d);
                }
            }
        }
        return v;
    }

    //Convierte "Chocolate, Frutilla" en una lista de sabores (lo contrario de saboresATexto)
    private List<Sabor> textoASabores(String texto) {
        List<Sabor> lista = new ArrayList<>();
        if (texto == null || texto.isBlank()) {
            return lista;
        }
        for (String nombre : texto.split(", ")) {
            lista.add(new Sabor(0, nombre, "", true));
        }
        return lista;
    }

    //Guarda en la base el nuevo estado de un envio. Si se cancela, devuelve los productos al stock
    public void actualizarEstadoEnvio(VentaADomicilio v) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            String sql = "UPDATE ventas SET estado_envio = ? WHERE id = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, v.getEstado().name());
                ps.setInt(2, v.getId());
                ps.executeUpdate();
            }
            if (v.getEstado() == EstadoEnvio.CANCELADO) {
                reponerStock(v);
            }
            conexion.commit();
        } catch (SQLException | RuntimeException e) {
            conexion.rollback(); //Si algo falla no se cambia el estado ni el stock
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    //Suma al stock lo que se habia vendido en la venta
    private void reponerStock(Venta v) throws SQLException {
        String sql = "UPDATE productos SET stock = stock + ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (DetalleVenta d : v.getDetalles()) {
                ps.setDouble(1, d.getCantidad());
                ps.setInt(2, d.getProducto().getId());
                ps.executeUpdate();
            }
        }
    }
}