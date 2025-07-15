package com.baeldung.lmock.service.defaultimpl;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.TaskRepository;

public class DefaultTaskServiceUnitTest {

    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    DefaultTaskService taskService;

    @BeforeEach
    public void setupDataSource() {
        MockitoAnnotations.openMocks(this);

        //given
        Task existingTask = new Task("Task 1", "Task 1 Description", LocalDate.now(), new Campaign("C1-CODE", "Campaign 1", "Campaign 1 Description"),
            TaskStatus.TO_DO, null);
        existingTask.setId(1L);
    }

    @Test
    void givenStubbedFindById_whenCalledMultipleTimes_thenSameResults() {
        // when
        Optional<Task> result1 = taskService.findById(2L);
        Optional<Task> result2 = taskService.findById(2L);
        Optional<Task> result3 = taskService.findById(2L);

        // then
        Assertions.assertTrue(result1.isEmpty());
        Assertions.assertTrue(result2.isEmpty());
        Assertions.assertTrue(result3.isEmpty());
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
        Assertions.assertTrue(result1.isPresent());
        Assertions.assertEquals(100L, result1.get()
            .getId());
        Assertions.assertTrue(result2.isPresent());
        Assertions.assertEquals(200L, result2.get()
            .getId());
        Assertions.assertTrue(result3.isEmpty());
    }
}
