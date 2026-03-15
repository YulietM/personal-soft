package com.personal.soft.repositories;

import com.personal.soft.models.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
    List<Transaccion> findByClienteIdOrderByFechaDesc(String clienteId);
}
