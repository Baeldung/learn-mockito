package com.baeldung.lmock.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.baeldung.lmock.domain.model.Task;

public interface TaskRepository {

    Optional<Task> findById(Long id);

    Task save(Task task);

    List<Task> findAll();

    List<Task> findByNameContainingAndAssigneeId(String name, Long assigneeId);

}