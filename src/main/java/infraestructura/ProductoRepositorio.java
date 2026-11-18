package infraestructura;
import clasesProducto.Producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import clasesProducto.ProductoEnvasado;
import clasesProducto.HeladoPorPeso;
import enums.TipoProducto;
import enums.PesoHelado;
import java.sql.Types;
import java.util.List;

public class ProductoRepositorio extends RepositorioJDBC<Producto> {

    public ProductoRepositorio() throws SQLException {
        super();
    }

    public void actualizarStockYPrecio(Producto p) throws SQLException {
        String sql = "UPDATE productos SET precio = ?, stock = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDouble(1, p.getPrecio());
            ps.setDouble(2, p.getStock());
            ps.setInt(3, p.getId());
            ps.executeUpdate();
        }
    }

    @Override
    protected String getTabla() {
        return "Productos";
    }

    @Override
    protected String getSqlInsert() {
        return "INSERT INTO Productos (nombre, precio, stock, tipo, tipo_producto, peso) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected Producto mapearFila(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio");
        int stock = rs.getInt("stock");

        String tipo = rs.getString("tipo");

        switch (tipo) {

            case "ENVASADO":
                return new ProductoEnvasado(
                        id,
                        nombre,
                        precio,
                        stock,
                        TipoProducto.valueOf(rs.getString("tipo_producto"))
                );

            case "POR_PESO":
                return new HeladoPorPeso(
                        id,
                        nombre,
                        precio,
                        stock,
                        PesoHelado.valueOf(rs.getString("peso"))
                );

            default:
                throw new SQLException("Tipo de producto desconocido: " + tipo);
        }
    }

    @Override
    protected void cargarInsert(PreparedStatement ps, Producto producto) throws SQLException {

        ps.setString(1, producto.getNombre());
        ps.setDouble(2, producto.getPrecio());
        ps.setInt(3, producto.getStock());

        if (producto instanceof ProductoEnvasado pe) {

            ps.setString(4, "ENVASADO");
            ps.setString(5, pe.getTipo().name());
            ps.setNull(6, Types.VARCHAR);

        } else if (producto instanceof HeladoPorPeso hp) {

            ps.setString(4, "POR_PESO");
            ps.setNull(5, Types.VARCHAR);
            ps.setString(6, hp.getPeso().name());

        } else {
            throw new SQLException("Tipo de producto no reconocido");
        }
    }

    @Override
    public void guardar(Producto producto) throws SQLException {
        super.guardar(producto);
    }

    @Override
    public Producto buscarPorId(int id) throws SQLException {
        return super.buscarPorId(id);
    }

    @Override
    public List<Producto> listarTodos() throws SQLException {
        return super.listarTodos();
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        return super.eliminar(id);
    }
}
