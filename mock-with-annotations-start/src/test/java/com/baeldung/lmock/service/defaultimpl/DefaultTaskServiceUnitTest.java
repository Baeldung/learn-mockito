package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.TaskRepository;
import com.baeldung.lmock.persistence.repository.inmemory.InMemoryTaskRepository;

class DefaultTaskServiceUnitTest {

    TaskRepository taskRepository;
    DefaultTaskService taskService;

    @BeforeEach
    void setupDataSource() {
        //given
        Task existingTask = new Task("Task 1", "Task 1 Description", LocalDate.now(), new Campaign("C1-CODE", "Campaign 1", "Campaign 1 Description"),
          TaskStatus.TO_DO, null);
        existingTask.setId(1L);
        taskRepository = new InMemoryTaskRepository(new HashSet<>(List.of(existingTask)));

        taskService = new DefaultTaskService(taskRepository);
    }

    @Test
    void givenExistingTask_whenFindById_thenTaskRetrieved() {
        // when
        Optional<Task> retrievedTask = taskService.findById(1L);

        // then
        assertEquals("Task 1", retrievedTask.get()
          .getName());
    }

    @Test
    void givenNonExistingTaskId_whenFindById_thenEmptyOptionalRetrieved() {
        // when
        Optional<Task> retrievedTask = taskService.findById(99L);

        // then
        assertTrue(retrievedTask.isEmpty());
    }

    @Test
    void givenExistingTask_whenUpdateStatus_thenUpdatedTaskSaved() {
        //given
        Task existingTask = new Task("Task 2", "Task 2 Description", LocalDate.now(), new Campaign("C2-CODE", "Campaign 2", "Campaign 2 Description"),
          TaskStatus.TO_DO, null);
        Task outputTask = taskService.create(existingTask);
        assertEquals(TaskStatus.TO_DO, outputTask.getStatus());

        // when
        Optional<Task> retrievedTask = taskService.updateStatus(existingTask.getId(), TaskStatus.IN_PROGRESS);

        // then
        assertEquals(TaskStatus.IN_PROGRESS, retrievedTask.get()
          .getStatus());
    }

}
