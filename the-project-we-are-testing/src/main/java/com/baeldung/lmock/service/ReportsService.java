package com.baeldung.lmock.service;

import com.baeldung.lmock.service.reports.ReportBuilder;

public interface ReportsService {

    public <T> T generateWorkerReport(ReportBuilder<T> builder, Long workerId);

    public <T> T generateManagerReport(ReportBuilder<T> builder);
}
