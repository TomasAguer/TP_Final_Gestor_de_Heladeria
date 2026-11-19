package infraestructura;

import clasesProducto.Sabor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class SaborDAO extends RepositorioJDBC<Sabor> {

    public SaborDAO() throws SQLException {
        super();
    }

    @Override
    protected String getTabla() {
        return "Sabores";
    }

    @Override
    protected String getSqlInsert() {
        return "INSERT INTO Sabores (nombre, descripcion, disponible) VALUES (?, ?, ?)";
    }

    @Override
    protected void cargarInsert(PreparedStatement ps, Sabor s)
            throws SQLException {

        ps.setString(1, s.getNombre());
        ps.setString(2, s.getDescripcion());
        ps.setBoolean(3, s.isDisponible());
    }

    @Override
    protected Sabor mapearFila(ResultSet rs) throws SQLException {
        return new Sabor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBoolean("disponible")
        );
    }

    @Override
    public void guardar(Sabor sabor) throws SQLException {
        super.guardar(sabor);
    }

    @Override
    public Sabor buscarPorId(int id) throws SQLException {
        return super.buscarPorId(id);
    }

    @Override
    public List<Sabor> listarTodos() throws SQLException {
        return super.listarTodos();
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        return super.eliminar(id);
    }
}