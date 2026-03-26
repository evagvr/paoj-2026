package com.pao.laboratory04.bonus.model;
import com.pao.laboratory04.bonus.enums.Status;
import com.pao.laboratory04.bonus.enums.Priority;
public class Task {
    private final String id;
    private final String title;
    private Status status;
    private final Priority priority;
    private String assignee;
    public Task(String id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = Status.TODO;
        this.assignee = null;
    }
    public String getId() {
        return id;
    }
    public String getTitle() { return title; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Priority getPriority() { return priority; }
    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                (assignee != null ? ", assignee='" + assignee + '\'' : ", assignee=null") +
                '}';
    }
}