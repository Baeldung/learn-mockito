package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
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
        Mockito.verify(taskRepository)
            .findById(2L);
        assertEquals(TaskStatus.IN_PROGRESS, retrievedTask.get()
            .getStatus());
    }

    @Test
    void whenSavingTask_thenIdIsAssigned() {
        // given
        when(taskRepository.save(any())).thenAnswer(invocation -> {
            Task taskArg = invocation.getArgument(0);
            taskArg.setId(999L);
            return taskArg;
        });
        Task task = aTask("Test Task");

        // when
        Task savedTask = taskService.create(task);

        // then
        assertEquals(999L, savedTask.getId());
    }

    @Test
    void givenConditionalSubbing_whenFindById_thenReturnCorrectTask() {
        // when
        when(taskRepository.findById(1L)).thenReturn(Optional.of(aTask("Task 1")));
        when(taskRepository.findById(2L)).thenReturn(Optional.of(aTask("Task 2")));
        when(taskRepository.findById(argThat(id -> id != 1L && id != 2L))).thenReturn(Optional.empty());

        // then
        assertTrue(taskService.findById(1L)
            .isPresent());
        assertTrue(taskService.findById(2L)
            .isPresent());
        assertTrue(taskService.findById(3L)
            .isEmpty());
    }

    @Test
    void givenAnswerApiStubbing_whenFindById_thenReturnCorrectTask() {
        // when
        when(taskRepository.findById(any())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return switch (id.intValue()) {
                case 1 -> Optional.of(aTask("Task 1"));
                case 2 -> Optional.of(aTask("Task 2"));
                default -> Optional.empty();
            };
        });

        // then
        assertTrue(taskService.findById(1L)
            .isPresent());
        assertTrue(taskService.findById(2L)
            .isPresent());
        assertTrue(taskService.findById(3L)
            .isEmpty());
    }

    @Test
    void whenDeleteTask_thenRemoveTaskId() {
        // given
        Task task = aTask("Test Task");
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        doAnswer(invocation -> {
            Task taskArg = invocation.getArgument(0, Task.class);
            taskArg.setId(null);
            return null;
        }).when(taskRepository)
            .delete(task);

        // when
        taskService.deleteTask(task.getId());

        // then
        assertNull(task.getId());
    }

    @Test
    void givenDeepStubbing_whenGetAssigneeFullName_thenUsedDeepStubName() {
        //given
        TaskRepository taskRepository = mock(TaskRepository.class, Mockito.RETURNS_DEEP_STUBS);
        TaskService taskService = new DefaultTaskService(taskRepository);

        when(taskRepository.getById(1L)
            .getAssignee()
            .getFirstName()
        ).thenReturn("John");

        when(taskRepository.getById(1L)
            .getAssignee()
            .getLastName()
        ).thenReturn("Doe");

        // when
        String fullName = taskService.getAssigneeFullName(1L);

        // then
        assertEquals("John DOE", fullName);
    }

    private static Task aTask(String name) {
        return new Task(name, "Task Description", LocalDate.now(), new Campaign("C3-CODE", "Campaign 3", "Campaign 3 Description"), TaskStatus.TO_DO, null);
    }
}
