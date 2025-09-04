package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
import com.baeldung.lmock.domain.model.Worker;
import com.baeldung.lmock.persistence.repository.TaskRepository;

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
    void givenInProgressTask_whenCompleteTask_thenTaskMarkedAsDoneAndCompletedAtRecorded() {
        // given
        Campaign campaign = new Campaign("C3-CODE", "Campaign 3", "Campaign 3 Description");
        Worker worker = new Worker("worker1@test.com", "Worker", "One");
        Task inProgressTask = new Task("Task 3", "Task 3 Description", LocalDate.now(), campaign , TaskStatus.IN_PROGRESS, worker);
        Long taskId = 1L;
        inProgressTask.setId(taskId);

        given(taskRepository.findById(taskId))
            .willReturn(Optional.of(inProgressTask));
        given(taskRepository.save(inProgressTask))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.completeTask(taskId);

        // then
        then(taskRepository)
            .should()
            .findById(taskId);
        then(taskRepository)
            .should()
            .save(assertArg(task -> {
                assertEquals(TaskStatus.DONE, task.getStatus());
                assertNotNull(task.getCompletedAt());
                assertEquals(worker, task.getAssignee());
            }));
    }

}
