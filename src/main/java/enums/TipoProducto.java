package enums;

public enum TipoProducto {
    PALITO_BOMBON(1500), PALITO_AGUA(1000), ESCOCES(2000), SUIZO(2000);

    private double precio;

    TipoProducto(double precio) {
        this.precio = precio;
    }
    public double getPrecio() {
        return precio;
    }

}
