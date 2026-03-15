package com.personal.soft.services;

import com.personal.soft.dtos.CrearClienteRequest;
import com.personal.soft.models.Cliente;
import com.personal.soft.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(String id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    public Cliente crearCliente(CrearClienteRequest request) {
        Cliente nuevoCliente = Cliente.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .celular(request.getCelular())
                .saldo(request.getSaldo()) 
                .preferenciaNotificacion(request.getPreferenciaNotificacion())
                .suscripcionesActivas(new ArrayList<>()) 
                .build();

        return clienteRepository.save(nuevoCliente);
    }
}
