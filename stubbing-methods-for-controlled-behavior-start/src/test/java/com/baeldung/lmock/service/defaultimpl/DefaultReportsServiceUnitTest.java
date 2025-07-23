package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

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
}
