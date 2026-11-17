package clasesProducto;

import enums.PesoHelado;

public class HeladoPorPeso extends Producto {
    private PesoHelado peso;

    ///Constuctor
    public HeladoPorPeso(String nombre, double precio, int stock, PesoHelado peso) {
        super(nombre, precio, stock);
        this.peso = peso;
    }

    public HeladoPorPeso() {
    }

    ///Constructor para la base de datos
    public HeladoPorPeso(int id, String nombre, double precio, int stock, PesoHelado peso) {
        super(id, nombre, precio, stock);
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "HeladoPorPeso{" +
                "peso=" + peso +
                "} " + super.toString();
    }

    public PesoHelado getPeso() {
        return peso;
    }

    ///Metodos


    @Override
    public double calcularImporte(double cantidad) {
        if (cantidad <=0){
            throw new IllegalArgumentException("El cantidad debe ser menor que 0");
        }
        double importe = getPrecio()*cantidad;

        if (peso == PesoHelado.KILO || cantidad>=2){ ///Llevando mas de dos kilos se le aplica un 25% de descuento.
            return importe -= importe*0.25;
        }
        return importe;
    }

}
