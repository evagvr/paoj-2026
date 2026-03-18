package com.pao.laboratory03.bonus.service;

import com.pao.laboratory03.bonus.enums.Priority;
import com.pao.laboratory03.bonus.enums.Status;
import com.pao.laboratory03.bonus.model.Task;
import com.pao.laboratory03.bonus.exceptions.DuplicateTaskException;
import com.pao.laboratory03.bonus.exceptions.InvalidTransitionException;
import com.pao.laboratory03.bonus.exceptions.TaskNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private static TaskService instance;
    private final Map<String, Task> tasksById;
    private final Map<Priority, List<Task>> tasksByPriority;
    private final List<String> auditLog;
    private int idCounter;

    private TaskService() {
        tasksById = new HashMap<>();
        tasksByPriority = new EnumMap<>(Priority.class);
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }
        auditLog = new ArrayList<>();
        idCounter = 1;
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }
    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", idCounter++);
        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task duplicat: " + id);
        }
        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");

        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        }
        task.setAssignee(assignee);
        System.out.println(taskId + " → " + assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }
    public void changeStatus(String taskId, Status newStatus) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost gasit");
        }
        Status oldStatus = task.getStatus();
        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }
        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + oldStatus + " → " + newStatus);
    }
    public List<Task> getTasksByPriority(Priority priority) {
        return tasksByPriority.getOrDefault(priority, Collections.emptyList());
    }
    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new EnumMap<>(Status.class);
        for (Status status : Status.values()) {
            summary.put(status, 0L);
        }
        for (Task task : tasksById.values()) {
            summary.put(task.getStatus(), summary.get(task.getStatus()) + 1);
        }
        return summary;
    }
    public List<Task> getUnassignedTasks() {
        return tasksById.values().stream().filter(task -> task.getAssignee() == null).collect(Collectors.toList());
    }
    public void printAuditLog() {
        for (String log : auditLog) {
            System.out.println(log);
        }
    }
    public double getTotalUrgencyScore(int baseDays) {
        return tasksById.values().stream()
                .filter(task -> task.getStatus() != Status.DONE && task.getStatus() != Status.CANCELLED)
                .mapToDouble(task -> task.getPriority().calculateScore(baseDays))
                .sum();
    }
    public Task addTaskWithId(String id, String title, Priority priority) {
        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task duplicat: " + id);
        }
        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");
        return task;
    }
}