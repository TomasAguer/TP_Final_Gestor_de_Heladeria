package interfaz;

import enums.EstadoEnvio;

public interface EnvioADomicilio {
    double calcularCostoEnvio();
    void actualizarEstado(EstadoEnvio nuevo);
    EstadoEnvio getEstado();

}
