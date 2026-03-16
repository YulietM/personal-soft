package com.personal.soft.repositories;

import com.personal.soft.models.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    Optional<Cliente> findByIdentificacion(Long identificacion);
}
