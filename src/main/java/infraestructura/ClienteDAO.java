package infraestructura;

import clasesUsuario.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO extends RepositorioJDBC<Cliente> {

    public ClienteDAO() throws SQLException{
        super();
    }

    @Override
    protected String getTabla(){
        return "Clientes";
    }

    @Override
    protected String getSqlInsert() {
        return "INSERT INTO clientes (nombre,email,telefono,direccion) VALUES(?,?,?,?)";
    }


    @Override
    protected void cargarInsert(PreparedStatement ps, Cliente c)throws SQLException{
        ps.setString(1,c.getNombre());
        ps.setString(2,c.getEmail());
        ps.setString(3,c.getTelefono());
        ps.setString(4,c.getDireccion());
    }

    @Override
    protected Cliente mapearFila(ResultSet rs) throws SQLException{
        return new Cliente(
                rs.getInt("ID"),
                rs.getString("Nombre"),
                rs.getString("Email"),
                rs.getString("Telefono"),
                rs.getString("Direccion")
        );
    }

}
