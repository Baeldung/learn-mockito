package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.TaskRepository;
import com.baeldung.lmock.persistence.repository.inmemory.InMemoryTaskRepository;
import com.baeldung.lmock.service.TaskValidator;

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
    void givenNewTask_whenCreateCalled_thenTaskIsSavedWithExpectedValues() {
        // given
        Task inputTask = new Task();
        inputTask.setId(null);
        inputTask.setName("Test Task");

        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(inputTask);

        // when
        Task result = taskService.create(inputTask);

        // then
        verify(taskRepository).save(Mockito.argThat(task ->
          task.getId() == null &&
          task.getStatus() == TaskStatus.TO_DO &&
          task.getAssignee() == null &&
          task.getName().equals("Test Task")
        ));
    }

    @Test
    void givenNewTask_whenCreateCalled_thenTaskIsSavedWithArgumentCaptor() {
        // given
        Task inputTask = new Task();
        inputTask.setId(null);
        inputTask.setName("Test Task");

        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(inputTask);

        // when
        Task result = taskService.create(inputTask);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());

        // then
        Task capturedTask = taskCaptor.getValue();
        assertNull(capturedTask.getId(), "Task ID should be null");
        assertEquals(TaskStatus.TO_DO, capturedTask.getStatus(), "Task status should be TO_DO");
        assertNull(capturedTask.getAssignee(), "Task assignee should be null");
        assertEquals("Test Task", capturedTask.getName(), "Task name should match input");
    }

    @Test
    void givenMockedStatic_whenCalled_thenSuccess() {
        try (MockedStatic<Task> validatorMock = Mockito.mockStatic(Task.class)) {
            // given
            validatorMock.when(() -> Task.validateTask(any(Task.class))).thenAnswer(invocation -> null);

            // when
            Task taskWithEmptyName = new Task();

            // then
            assertDoesNotThrow(() -> Task.validateTask(taskWithEmptyName));

            validatorMock.verify(() -> Task.validateTask(taskWithEmptyName));
        }
    }

    @Test
    void givenFinalMethod_whenCalled_thenSuccess() {
        // given
        DefaultTaskService serviceMock = Mockito.mock(DefaultTaskService.class);

        Task allowedTask = new Task();
        allowedTask.setStatus(TaskStatus.TO_DO);

        Task blockedTask = new Task();
        blockedTask.setStatus(TaskStatus.DONE);

        // when
        when(serviceMock.canReassignTask(allowedTask)).thenReturn(false);
        when(serviceMock.canReassignTask(blockedTask)).thenReturn(true);

        // then
        assertFalse(serviceMock.canReassignTask(allowedTask));
        assertTrue(serviceMock.canReassignTask(blockedTask));
    }

    @Test
    void givenFinalClass_whenCalledMethod_thenSuccess() {
        // given
        TaskValidator validatorMock = Mockito.mock(TaskValidator.class);

        Task toDoTask = new Task();
        toDoTask.setStatus(TaskStatus.TO_DO);

        Task doneTask = new Task();
        doneTask.setStatus(TaskStatus.DONE);

        // when
        when(validatorMock.canReassignTask(toDoTask)).thenReturn(true);
        when(validatorMock.canReassignTask(doneTask)).thenReturn(false);
        when(validatorMock.canReassignTask(null)).thenReturn(false);

        // then
        assertTrue(validatorMock.canReassignTask(toDoTask), "Tasks in TO_DO status should be reassignable");
        assertFalse(validatorMock.canReassignTask(doneTask), "Tasks in DONE status should not be reassignable");
        assertFalse(validatorMock.canReassignTask(null), "Null tasks should not be reassignable");
    }

    @Test
    void givenMockedRepository_whenCreateConstructor_thenSuccessConstructorWithRepository() {
        // given
        TaskRepository mockRepository = Mockito.mock(TaskRepository.class);

        // when
        DefaultTaskService service = new DefaultTaskService(mockRepository);

        // then
        assertSame(mockRepository, service.getTaskRepository());
    }

    @Test
    void givenMockedConstructor_whenCreateDefaultConstructor_thenSuccess() {
        // given
        try (MockedConstruction<InMemoryTaskRepository> mocked = mockConstruction(InMemoryTaskRepository.class)) {
            // when
            DefaultTaskService service = new DefaultTaskService();

            // then
            assertEquals(1, mocked.constructed().size());

            TaskRepository mockRepository = mocked.constructed().get(0);
            assertSame(mockRepository, service.getTaskRepository());
        }
    }

}