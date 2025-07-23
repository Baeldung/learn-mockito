package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.TaskRepository;

class DefaultTaskServiceUnitTest {

    TaskRepository taskRepository;
    DefaultTaskService taskService;

    @BeforeEach
    void setupDataSource() {
        //given
        taskRepository = Mockito.mock(TaskRepository.class);

        taskService = new DefaultTaskService(taskRepository);
    }

    @Test
    void givenExistingTask_whenFindById_thenTaskRetrieved() {
        //given
        Task existingTask = new Task("Task 1", "Task 1 Description", LocalDate.now(), new Campaign("C1-CODE", "Campaign 1", "Campaign 1 Description"),
          TaskStatus.TO_DO, null);
        Mockito.when(taskRepository.findById(1L))
          .thenReturn(Optional.of(existingTask));
        // when
        Optional<Task> retrievedTask = taskService.findById(1L);

        // then
        assertEquals("Task 1", retrievedTask.get()
          .getName());
    }
}
