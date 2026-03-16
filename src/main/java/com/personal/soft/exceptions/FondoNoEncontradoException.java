package com.personal.soft.exceptions;

public class FondoNoEncontradoException extends EntidadNoEncontradaException {
    public FondoNoEncontradoException(String id) {
        super("El fondo con ID " + id + " no existe.");
    }
}
