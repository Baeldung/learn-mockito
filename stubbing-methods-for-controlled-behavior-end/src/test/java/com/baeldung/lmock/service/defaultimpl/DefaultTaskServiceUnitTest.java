package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.TaskRepository;
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
}
