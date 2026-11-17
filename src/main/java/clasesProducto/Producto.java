package clasesProducto;

import excepciones.StockInsuficienteException;
import excepciones.VentaInvalidaException;
import interfaz.Identificable;

import java.util.Objects;

public abstract class Producto implements Comparable<Producto>, Identificable { //Implementar la interfaz comparable!

    private int id;
    private String nombre;
    private double precio;
    private int stock;

    ///Constructor
    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto(){
    }

    /// Constructor para la base de datos
    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    ///Gets and Sets
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    ///Metodos
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Producto producto)) return false;
        return id == producto.id && precio == producto.precio && stock == producto.stock && Objects.equals(nombre, producto.nombre);
    }

    @Override
    public int compareTo(Producto otro) {
        return Double.compare(this.precio, otro.precio);
    }

    public  void actualizarStock(double cantidad)throws StockInsuficienteException{
        if (stock < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente");
        }
        stock = (int) (stock - cantidad);

        System.out.println("Stock insuficiente");
    }
    public abstract double calcularImporte(double cantidad);

}