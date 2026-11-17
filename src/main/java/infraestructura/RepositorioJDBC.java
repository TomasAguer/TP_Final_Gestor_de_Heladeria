package infraestructura;

import interfaz.Identificable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class RepositorioJDBC<T extends Identificable> {
    protected Connection conexion;

    public RepositorioJDBC() throws SQLException{
        this.conexion = ConexionDB.getConnection();
    }

    protected abstract String getTabla();
    protected abstract String getSqlInsert();
    protected abstract T mapearFila(ResultSet rs) throws SQLException;
    protected abstract void cargarInsert(PreparedStatement ps, T t) throws SQLException;

    public void guardar(T t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(getSqlInsert(), Statement.RETURN_GENERATED_KEYS)) {
            cargarInsert(ps, t);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setId(rs.getInt(1));
                }
            }
        }
    }

    public T buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM " + getTabla() + " WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFila(rs);
                }
            }
        }
        return null;
    }

    public List<T> listarTodos() throws SQLException {
        List<T> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + getTabla();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        }
        return lista;
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM " + getTabla() + " WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}

