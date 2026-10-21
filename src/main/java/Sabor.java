public class Sabor {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean disponible;

    public Sabor(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.disponible = true;
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

    public int getId() {
        return id;
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
