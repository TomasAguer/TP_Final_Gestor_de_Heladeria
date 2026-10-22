public class HeladoPorPeso extends Producto{

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
    public double calcularImporte() {
        return 0;
    }

}
