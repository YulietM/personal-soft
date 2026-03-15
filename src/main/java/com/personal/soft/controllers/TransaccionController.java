package com.personal.soft.controllers;

import com.personal.soft.models.Transaccion;
import com.personal.soft.services.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transacciones")
@RequiredArgsConstructor
@Tag(name = "Transacciones API", description = "Endpoints para consultar el historial")
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener historial de transacciones de un cliente (aberturas y cancelaciones)")
    public ResponseEntity<List<Transaccion>> obtenerHistorial(@PathVariable String clienteId) {
        return ResponseEntity.ok(transaccionService.obtenerHistorial(clienteId));
    }
}
