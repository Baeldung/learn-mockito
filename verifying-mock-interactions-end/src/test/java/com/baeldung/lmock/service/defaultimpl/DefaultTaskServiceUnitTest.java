package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
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
        assertEquals("Task 1", retrievedTask.get()
            .getName());
        verify(taskRepository).findById(1L);
    }

    @Test
    void givenNonExistingTaskId_whenFindById_thenEmptyOptionalRetrieved() {
        // when
        Optional<Task> retrievedTask = taskService.findById(99L);

        // then
        assertTrue(retrievedTask.isEmpty());
    //        verify(taskRepository).findById(any());
    //        verify(taskRepository).findById(anyLong());
        verify(taskRepository)
          .findById(argThat(id -> id > 50L && id < 100L));
    }

    @Test 
    void whenSearchTasks_thenRepositoryIsInvoked() { 
        // when 
        List<Task> tasks = taskService.searchTasks("name", 100L); 
        
        // then 
        assertTrue(tasks.isEmpty());
        verify(taskRepository)
          .findByNameContainingAndAssigneeId(eq("name"), argThat(id -> id > 50L)); 
    }

    @Test
    void whenCreateAndUpdate_thenRepositoryIsInvokedTwice() {
        //given
        Task task = new Task("Task 1", "Task 1 Description", LocalDate.now(), new Campaign("C2-CODE", "Campaign 2", "Campaign 2 Description"),
          TaskStatus.TO_DO, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        taskService.create(task);
        task.setId(1L);
        task.setName("Task 1 Updated");
        taskService.updateTask(task.getId(), task);

        // then
        verify(taskRepository, times(2)).save(any());
    }

    @Test
    void whenUpdatingNonExistingTask_thenRepositoryIsNotInvoked() {
        //given
        Task task = new Task("Task 1", "Task 1 Description", LocalDate.now(), new Campaign("C2-CODE", "Campaign 2", "Campaign 2 Description"),
            TaskStatus.TO_DO, null);

        // when
        taskService.updateTask(999L, task);

        // then
        verify(taskRepository, never()).save(any());
    }

    @Test
    void givenExistingTask_whenUpdateStatus_thenUpdatedTaskSaved() {
        //given
        InOrder inOrder = Mockito.inOrder(taskRepository);
        Task existingTask = new Task("Task 2", "Task 2 Description", LocalDate.now(), new Campaign("C2-CODE", "Campaign 2", "Campaign 2 Description"),
            TaskStatus.TO_DO, null);
        existingTask.setId(2L);

        // when
        when(taskRepository.findById(2L))
          .thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask))
          .thenReturn(existingTask);

        Optional<Task> retrievedTask = taskService.updateStatus(2L, TaskStatus.IN_PROGRESS);

        // then
        assertEquals(TaskStatus.IN_PROGRESS, retrievedTask.get()
            .getStatus());
        inOrder.verify(taskRepository).findById(2L);
        inOrder.verify(taskRepository).save(existingTask);
    }

}
