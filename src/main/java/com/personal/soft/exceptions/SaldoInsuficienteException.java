package com.personal.soft.exceptions;

public class SaldoInsuficienteException extends ReglaNegocioException {
    public SaldoInsuficienteException(String fondoNombre) {
        super("No tiene saldo disponible para vincularse al fondo " + fondoNombre);
    }
}
