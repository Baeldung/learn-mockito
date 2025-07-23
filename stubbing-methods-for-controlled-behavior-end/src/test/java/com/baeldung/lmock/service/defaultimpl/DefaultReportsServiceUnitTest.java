package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.service.TaskService;
import com.baeldung.lmock.service.reports.ReportBuilder;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultReportsServiceUnitTest {

    @Mock
    private TaskService taskService;
    @InjectMocks
    private DefaultReportsService defaultReportsService;

    @Test
    void givenDefaultReportsService_whenGenerateManagerReportWithoutStubbingVoid_thenReturnReports() {
        // given
        ReportBuilder<String> reportBuilder = Mockito.mock(ReportBuilder.class);

        // when
        String generatedManagerReport = defaultReportsService.generateManagerReport(reportBuilder);

        // then
        assertNull(generatedManagerReport);
    }

    @Test
    void givenDefaultReportsService_whenGenerateManagerReportWithStubbingVoid_thenReturnReports() {
        // given
        List<Task> relevantTasks = new ArrayList<>();
        when(taskService.searchTasks(null, null)).thenReturn(relevantTasks);
        ReportBuilder<String> reportBuilder = Mockito.mock(ReportBuilder.class);
        doNothing().when(reportBuilder)
          .addTasksData(relevantTasks);
        doNothing().when(reportBuilder)
          .addCampaignsData(relevantTasks);
        doNothing().when(reportBuilder)
          .addWorkersData(relevantTasks);
        when(reportBuilder.obtainReport()).thenReturn("report results");

        // when
        String generatedManagerReport = defaultReportsService.generateManagerReport(reportBuilder);

        // then
        assertEquals("report results", generatedManagerReport);
    }
}
