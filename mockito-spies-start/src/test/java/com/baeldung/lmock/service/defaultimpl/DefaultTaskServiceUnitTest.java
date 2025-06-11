package com.baeldung.lmock.service.defaultimpl;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class DefaultTaskServiceUnitTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    DefaultTaskService taskService;

    @Test
    public void givenExistingTask_whenFindById_thenTaskRetrieved() {
        //given
        Task existingTask = new Task("Task 1", "Task 1 Description", LocalDate.now(), new Campaign("C1-CODE", "Campaign 1", "Campaign 1 Description"),
            TaskStatus.TO_DO, null);
        existingTask.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // when
        Optional<Task> retrievedTask = taskService.findById(1L);

        // then
        Mockito.verify(taskRepository)
            .findById(1L);
        Assertions.assertEquals("Task 1", retrievedTask.get()
            .getName());
    }

    @Test
    public void givenNonExistingTaskId_whenFindById_thenEmptyOptionalRetrieved() {
        // when
        Optional<Task> retrievedTask = taskService.findById(99L);

        // then
        Assertions.assertTrue(retrievedTask.isEmpty());
        Mockito.verify(taskRepository)
            .findById(99L);
    }

    @Test
    public void givenExistingTask_whenUpdateStatus_thenUpdatedTaskSaved() {
        //given
        Task existingTask = new Task("Task 2", "Task 2 Description", LocalDate.now(), new Campaign("C2-CODE", "Campaign 2", "Campaign 2 Description"),
            TaskStatus.TO_DO, null);
        existingTask.setId(2L);

        // when
        when(taskRepository.findById(2L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Optional<Task> retrievedTask = taskService.updateStatus(2L, TaskStatus.IN_PROGRESS);

        // then
        Mockito.verify(taskRepository)
            .findById(2L);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, retrievedTask.get()
            .getStatus());
    }

}
