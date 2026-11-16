package interfaz;

import enums.EstadoEnvio;
import excepciones.VentaInvalidaException;

public interface EnvioADomicilio {
    double calcularCostoEnvio();
    void actualizarEstado(EstadoEnvio nuevo) throws VentaInvalidaException;
    EstadoEnvio getEstado();

}
