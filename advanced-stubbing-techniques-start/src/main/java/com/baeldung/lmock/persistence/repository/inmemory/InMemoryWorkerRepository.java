package com.baeldung.lmock.persistence.repository.inmemory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.baeldung.lmock.domain.model.Worker;
import com.baeldung.lmock.persistence.repository.WorkerRepository;

public class InMemoryWorkerRepository implements WorkerRepository {

    private Set<Worker> workers;

    public InMemoryWorkerRepository() {
        super();
        this.workers = new HashSet<>();
    }

    public InMemoryWorkerRepository(Set<Worker> workers) {
        super();
        this.workers = workers;
    }

    @Override
    public Optional<Worker> findById(Long id) {
        return workers.stream()
            .filter(p -> p.getId()
                .equals(id))
            .findFirst();
    }

    @Override
    public Worker save(Worker worker) {
        Long workerId = worker.getId();
        if (workerId == null) {
            worker.setId(new Random().nextLong(Long.MAX_VALUE));
        } else {
            findById(workerId).ifPresent(workers::remove);
        }
        workers.add(worker);
        return worker;
    }
}
