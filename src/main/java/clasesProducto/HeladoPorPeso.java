package clasesProducto;

import enums.PesoHelados;

public class HeladoPorPeso extends Producto {
    private PesoHelados peso;

    ///Constuctor
    public HeladoPorPeso(String nombre, double precio, int stock, PesoHelados peso) {
        super(nombre, precio, stock);
        this.peso = peso;
    }

    public HeladoPorPeso() {
    }

    ///Constructor para la base de datos
    public HeladoPorPeso(int id, String nombre, double precio, int stock, PesoHelados peso) {
        super(id, nombre, precio, stock);
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "HeladoPorPeso{" +
                "peso=" + peso +
                "} " + super.toString();
    }

    ///Metodos
    @Override
    public double calcularImporte(double cantidad) {
        double importe = 0;
        if (cantidad <=0){
            throw new IllegalArgumentException("El cantidad debe ser menor que 0");
        }
        importe = peso.getPrecio()*cantidad;
        if (peso == PesoHelados.KILO || cantidad>=2){
            return importe -= importe*0.25;
        }
        return importe;
    }

}
