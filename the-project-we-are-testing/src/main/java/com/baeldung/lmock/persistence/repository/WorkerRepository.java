package com.baeldung.lmock.persistence.repository;

import java.util.Optional;

import com.baeldung.lmock.domain.model.Worker;

public interface WorkerRepository {

    Optional<Worker> findById(Long id);

    Worker save(Worker worker);

}
