package com.baeldung.lmock.service.defaultimpl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.TaskRepository;
import com.baeldung.lmock.persistence.repository.inmemory.InMemoryTaskRepository;

public class DefaultTaskServiceUnitTest {

    TaskRepository taskRepository;
    DefaultTaskService taskService;

    @BeforeEach
    public void setupDataSource() {
        //given
        Task existingTask = new Task("Task 1", "Task 1 Description", LocalDate.now(), new Campaign("C1-CODE", "Campaign 1", "Campaign 1 Description"),
            TaskStatus.TO_DO, null);
        existingTask.setId(1L);
        taskRepository = new InMemoryTaskRepository(new HashSet<>(List.of(existingTask)));

        taskService = new DefaultTaskService(taskRepository);
    }

    @Test
    public void givenExistingTask_whenFindById_thenTaskRetrieved() {
        // when
        Optional<Task> retrievedTask = taskService.findById(1L);

        // then
        Assertions.assertEquals("Task 1", retrievedTask.get()
            .getName());
    }
}
