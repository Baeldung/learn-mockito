package com.baeldung.lmock.service.defaultimpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.baeldung.lmock.service.TaskService;
import com.baeldung.lmock.service.reports.ReportBuilder;

class DefaultReportsServiceUnitTest {

    @Mock
    private TaskService taskService;
    @InjectMocks
    private DefaultReportsService defaultReportsService;

    @BeforeEach
    public void setupDataSource() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenDefaultReportsService_whenGenerateManagerReportWithoutStubbingVoid_thenReturnReports() {
        // given
        ReportBuilder<String> reportBuilder = Mockito.mock(ReportBuilder.class);

        // when
        String generatedManagerReport = defaultReportsService.generateManagerReport(reportBuilder);

        // then
        Assertions.assertNull(generatedManagerReport);
    }
}
