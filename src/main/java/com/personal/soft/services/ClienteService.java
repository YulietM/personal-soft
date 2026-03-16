package com.personal.soft.services;

import com.personal.soft.dtos.CrearClienteRequest;
import com.personal.soft.models.Cliente;
import com.personal.soft.repositories.ClienteRepository;
import com.personal.soft.dtos.ActualizarClienteRequest;
import com.personal.soft.exceptions.EntidadNoEncontradaException;
import com.personal.soft.exceptions.EntidadYaExisteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new EntidadNoEncontradaException("Cliente con ID " + id + " no encontrado"));
    }


    public Cliente crearCliente(CrearClienteRequest request) {
        if (clienteRepository.findByIdentificacion(request.getIdentificacion()).isPresent()) {
            throw new EntidadYaExisteException("Ya existe un cliente con la identificación proporcionada: " + request.getIdentificacion());
        }

        Cliente nuevoCliente = Cliente.builder()
                .identificacion(request.getIdentificacion())
                .nombre(request.getNombre())
                .email(request.getEmail())
                .celular(request.getCelular())
                .saldo(request.getSaldo()) 
                .preferenciaNotificacion(request.getPreferenciaNotificacion())
                .suscripcionesActivas(new ArrayList<>()) 
                .build();

        return clienteRepository.save(nuevoCliente);
    }

    public Cliente actualizarCliente(String id, ActualizarClienteRequest request) {
        Cliente cliente = obtenerPorId(id);
        
        if (request.getEmail() != null) {
            cliente.setEmail(request.getEmail());
        }
        
        if (request.getCelular() != null) {
            cliente.setCelular(request.getCelular());
        }
        
        return clienteRepository.save(cliente);
    }
}
