public class ProductoEnvasado extends Producto {

    private String tipoProducto;

    ///Constructor
    public ProductoEnvasado(String nombre, int precio, int stock, String tipoProducto) {
        super(nombre, precio, stock);
        this.tipoProducto = tipoProducto;
    }
    public ProductoEnvasado(){
    }

    ///Gets and Sets
    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    ///Metodos
    @Override
    public double calcularImporte() {
        return 0;
    }

    @Override
    public String toString() {
        return "ProductoEnvasado{" +
                "tipoProducto='" + tipoProducto + '\'' +
                "} " + super.toString();
    }

}
