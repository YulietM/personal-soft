package com.personal.soft.controllers;

import com.personal.soft.dtos.MensajeResponse;
import com.personal.soft.dtos.SuscripcionRequest;
import com.personal.soft.models.Fondo;
import com.personal.soft.repositories.FondoRepository;
import com.personal.soft.services.FondoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fondos")
@RequiredArgsConstructor
@Tag(name = "Fondos API", description = "Endpoints para listar, suscribir y cancelar fondos")
public class FondoController {

    private final FondoService fondoService;
    private final FondoRepository fondoRepository;

    @GetMapping
    @Operation(summary = "Listar todos los fondos disponibles")
    public ResponseEntity<List<Fondo>> listarFondos() {
        return ResponseEntity.ok(fondoRepository.findAll());
    }

    @PostMapping("/suscripciones")
    @Operation(summary = "Suscribir un cliente a un fondo")
    public ResponseEntity<MensajeResponse> suscribirse(@Valid @RequestBody SuscripcionRequest request) {
        fondoService.suscribirse(request.getClienteId(), request.getFondoId(), request.getMonto());
        return ResponseEntity.ok(new MensajeResponse("Suscripción realizada exitosamente."));
    }

    @DeleteMapping("/{fondoId}/suscripciones")
    @Operation(summary = "Cancelar suscripción a un fondo")
    public ResponseEntity<MensajeResponse> cancelarSuscripcion(@PathVariable String fondoId,
                                                               @RequestParam String clienteId) {
        fondoService.cancelarSuscripcion(clienteId, fondoId);
        return ResponseEntity.ok(new MensajeResponse("Suscripción cancelada exitosamente."));
    }
}
