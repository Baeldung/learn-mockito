package com.baeldung.lmock.service.defaultimpl;

import com.baeldung.lmock.persistence.repository.WorkerRepository;
import com.baeldung.lmock.service.TaskService;
import com.baeldung.lmock.service.reports.SimpleMapReportBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DefaultReportsServiceTest {

    @Mock
    TaskService taskService;

    @Mock
    WorkerRepository workerRepository;

    @InjectMocks
    DefaultWorkerService workerService;

    @InjectMocks
    private DefaultReportsService reportsService;

    @Test
    void givenWorkerIdAndReportBuilder_whenGenerateWorkerReport_thenNPE() {

        var builder = new SimpleMapReportBuilder();

        // Mockito won't inject the created workerService into the reportsService, so
        // we get a NullPointerException here
        assertThrows(NullPointerException.class,() -> reportsService.generateWorkerReport(builder,1L));
    }

    @Test
    void givenWorkerIdAndReportBuilder_whenGenerateWorkerReport_thenReportNotFound() {

        var builder = new SimpleMapReportBuilder();
        var rs = new DefaultReportsService(taskService,workerService);

        // Here, we also get an exception because the generated mock workService returns an empty Optional
        assertThrows(NoSuchElementException.class,() -> rs.generateWorkerReport(builder,1L));

    }

}