package com.tucasaca.tienda.exception;

// Excepción general para cuando no se encuentra un recurso en la db
// (producto, usuario, pedido, categoria, casaca, etc.)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super("No se encontró " + resourceName + " con id: " + id);
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super("No se encontró " + resourceName + " con identificador: " + identifier);
    }
}
