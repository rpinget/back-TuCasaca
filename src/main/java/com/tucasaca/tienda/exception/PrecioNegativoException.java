package com.tucasaca.tienda.exception;

/**
 * Excepción personalizada que se lanza cuando se intenta guardar un producto con un precio negativo.
 * Esta excepción extiende de IllegalArgumentException y proporciona un mensaje de error específico.
 * PrecioNegativoException
 */
public class PrecioNegativoException extends IllegalArgumentException {
    public PrecioNegativoException() {
        super("El precio no puede ser negativo");
    }

    public PrecioNegativoException(String message) {
        super(message);
    }
}
    