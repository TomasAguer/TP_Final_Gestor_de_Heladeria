package enums;

public enum PesoHelados {
    CUARTO(8000), MEDIO(15000), KILO(25000);

    private double precio;
    PesoHelados(double precio) {
        this.precio = precio;
    }
    public double getPrecio() {
        return precio;
    }
}
