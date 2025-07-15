package com.baeldung.lmock.service;

import com.baeldung.lmock.domain.model.Task;
import com.baeldung.lmock.domain.model.TaskStatus;

public final class TaskValidator {
    public boolean canReassignTask(Task task) {
        return task != null &&
          (task.getStatus() == TaskStatus.TO_DO ||
          task.getStatus() == TaskStatus.IN_PROGRESS);
    }
}
