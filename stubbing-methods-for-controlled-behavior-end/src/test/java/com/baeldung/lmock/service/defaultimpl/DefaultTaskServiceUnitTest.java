package com.baeldung.lmock.service.defaultimpl;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.TaskRepository;
import com.baeldung.lmock.service.defaultimpl.DefaultTaskService;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultTaskServiceUnitTest {

    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    DefaultTaskService taskService;

    @Test
    void givenStubbedFindById_whenCalledMultipleTimes_thenSameResults() {
        // when
        Optional<Task> result1 = taskService.findById(2L);
        Optional<Task> result2 = taskService.findById(2L);
        Optional<Task> result3 = taskService.findById(2L);

        // then
        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
        assertTrue(result3.isEmpty());
    }

    @Test
    void givenStubbedFindById_whenCalledMultipleTimes_thenDifferentResults() {
        // given
        Campaign campaign = new Campaign();
        Task firstTask = new Task("First", "desc", LocalDate.now(), campaign, TaskStatus.TO_DO, null);
        firstTask.setId(100L);
        Task secondTask = new Task("Second", "desc2", LocalDate.now(), campaign, TaskStatus.TO_DO, null);
        secondTask.setId(200L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(firstTask))
            .thenReturn(Optional.of(secondTask))
            .thenReturn(Optional.empty());

        // when
        Optional<Task> result1 = taskService.findById(1L);
        Optional<Task> result2 = taskService.findById(1L);
        Optional<Task> result3 = taskService.findById(1L);

        // then
        assertTrue(result1.isPresent());
        assertEquals(100L, result1.get()
            .getId());
        assertTrue(result2.isPresent());
        assertEquals(200L, result2.get()
            .getId());
        assertTrue(result3.isEmpty());
    }

    @Test
    @DisplayName("should search for tasks and return an empty list when none are found")
    void givenAnyStringAndAnyLong_whenSearchTasks_thenReturnsEmptyList() {
        // 1. Arrange: Stub the repository method to return an empty list.
        when(taskRepository.findByNameContainingAndAssigneeId(anyString(), anyLong()))
            .thenReturn(Collections.emptyList());

        // 2. Act: Call the service method and capture its return value.
        String searchName = "test";
        Long assigneeId = 1L;
        List<Task> actualTasks = taskService.searchTasks(searchName, assigneeId);

        // 3. Assert: Use JUnit 5 assertions to check the state of the returned list.
        assertNotNull(actualTasks, "The result should not be null.");
        assertTrue(actualTasks.isEmpty(), "The returned list should be empty.");
    }
    
    @Test
    @DisplayName("should search with any name and an exact assignee ID")
    void givenAnySearchAndExactAssigneeId_whenSearchTasks_thenRepositoryIsCalled() {
       
        when(taskRepository.findByNameContainingAndAssigneeId(any(), eq(1L)))
            .thenReturn(Collections.emptyList());

        // Call the method under test.
        String searchName = "test";
        Long assigneeId = 1L;
        List<Task> result = taskService.searchTasks(searchName, assigneeId);
        
        //Assert the final output of the service method
        assertNotNull(result, "The result should not be null.");
        assertTrue(result.isEmpty());
    }
}
