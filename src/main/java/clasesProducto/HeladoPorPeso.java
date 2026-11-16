package clasesProducto;

public class HeladoPorPeso extends Producto {

    ///Constuctor
    public HeladoPorPeso(String nombre, int precio, int stock) {
        super(nombre, precio, stock);
    }

    public HeladoPorPeso() {
    }

    ///Metodos
    @Override
    public String toString() {
        return "HeladoPorPeso{} " + super.toString();
    }

    @Override
    //Llevando 2kilos o mas medio kilo de regalo
    public double calcularImporte(double cantidad) {
        double importe = getPrecio()*cantidad;
        if (cantidad >=2){
            importe = importe - (getPrecio()*0.5);
        }

        return importe;
    }

}
