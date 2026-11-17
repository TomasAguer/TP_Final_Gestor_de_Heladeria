package clasesProducto;

import interfaz.Identificable;

public class Sabor implements Identificable {

    private int id;
    private String nombre;
    private String descripcion;
    private boolean disponible;

    public Sabor(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.disponible = true;
    }
    ///Constructor para la base de datos:concepto llamado sobrecarga de constructores
    public Sabor(int id, String nombre, String descripcion, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.disponible = disponible;
    }

    public void activar(){
        this.disponible = true;
    }

    public void desactivar(){
        this.disponible = false;
    }

    public void modificar(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public String toString() {
      return nombre + (disponible ? "" : " (Disponible)");
    }
}
