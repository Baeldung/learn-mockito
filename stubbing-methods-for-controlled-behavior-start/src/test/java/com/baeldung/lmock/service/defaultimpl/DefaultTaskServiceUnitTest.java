package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.baeldung.lmock.domain.model.Task;
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
}
