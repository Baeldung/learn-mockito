package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

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
import com.baeldung.lmock.service.TaskService;

@ExtendWith(MockitoExtension.class)
class DefaultTaskServiceUnitTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    DefaultTaskService taskService;

    @Test
    void givenExistingTask_whenFindById_thenTaskRetrieved() {
        // given
        Task existingTask = new Task("Task 1", "Task 1 Description", LocalDate.now(), new Campaign("C1-CODE", "Campaign 1", "Campaign 1 Description"),
            TaskStatus.TO_DO, null);
        existingTask.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // when
        Optional<Task> retrievedTask = taskService.findById(1L);

        // then
        Mockito.verify(taskRepository)
            .findById(1L);
        assertEquals("Task 1", retrievedTask.get()
            .getName());
    }

    @Test
    void givenNonExistingTaskId_whenFindById_thenEmptyOptionalRetrieved() {
        // when
        Optional<Task> retrievedTask = taskService.findById(99L);

        // then
        assertTrue(retrievedTask.isEmpty());
        Mockito.verify(taskRepository)
            .findById(99L);
    }

    @Test
    void givenExistingTask_whenUpdateStatus_thenUpdatedTaskSaved() {
        //given
        Task existingTask = new Task("Task 2", "Task 2 Description", LocalDate.now(), new Campaign("C2-CODE", "Campaign 2", "Campaign 2 Description"),
            TaskStatus.TO_DO, null);
        existingTask.setId(2L);

        // when
        when(taskRepository.findById(2L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Optional<Task> retrievedTask = taskService.updateStatus(2L, TaskStatus.IN_PROGRESS);

        // then
        assertEquals(TaskStatus.IN_PROGRESS, retrievedTask.get()
            .getStatus());
    }

    @Test
    void givenATaskWithId5_whenFindById_thenTaskRetrieved() {
        // given
        Task actualTask = new Task("Task 5", "Task 5 Description", LocalDate.now(), new Campaign("C5-CODE", "Campaign 5", "Campaign 5 Description"),
            TaskStatus.TO_DO, null);
        actualTask.setId(5L);
        when(taskRepository.findById(5L)).thenReturn(Optional.of(actualTask));

        // when
        Task retrievedTask = taskService.findById(5L)
            .get();

        // then
        assertEquals("Task 5", retrievedTask.getName());
        assertEquals(5L, retrievedTask.getId());
    }

    @Test
    void givenATaskWithId6_whenFindById_thenTaskRetrieved() {
        // given
        TaskRepository mockedRepo = mock();
        TaskService actualService = new DefaultTaskService(mockedRepo);
        when(mockedRepo.findById(6L)).thenReturn(Optional.empty());

        // when
        Optional<Task> retrievedTask = actualService.findById(6L);

        // then
        assertTrue(retrievedTask.isEmpty());
    }

    @Test
    void givenATaskWithId7_whenFindById_thenTaskRetrieved() {
        // given
        Task actualTask = new Task("Task 7", "Task 7 Description", LocalDate.now(),
            new Campaign("C7-CODE", "Campaign 7", "Campaign 7 Description"), TaskStatus.TO_DO, null);

        when(taskRepository.findById(7L)).thenReturn(Optional.of(actualTask));

        TaskService service = new DefaultTaskService(taskRepository);

        // when
        String retrievedTaskName = service.findById(7L)
            .get()
            .getName();

        // then
        assertEquals("Task 7", retrievedTaskName);
    }
}
