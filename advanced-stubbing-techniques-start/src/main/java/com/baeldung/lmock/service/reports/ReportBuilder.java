package com.baeldung.lmock.service.reports;

import java.util.List;

import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.Worker;

public interface ReportBuilder<T> {

    void addWorkersData(List<Task> tasks);

    void addCampaignsData(List<Task> tasks);

    void addTasksData(List<Task> tasks);

    void addSpecificWorkerData(Worker worker);

    T obtainReport();
}
