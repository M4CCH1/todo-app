package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class Todo {

    // 既存項目
    private int id;
    private String title;
    private boolean completed;

    // モックアップ仕様で追加する項目
    private LocalDate registrationDate;
    private LocalTime registrationTime;
    private String taskContent;
    private int taskProgress;
    private String taskNote;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 既存機能で使用しているコンストラクタ
    public Todo(int id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    // 新しいTodoデータ用のコンストラクタ
    public Todo(
            int id,
            String title,
            boolean completed,
            LocalDate registrationDate,
            LocalTime registrationTime,
            String taskContent,
            int taskProgress,
            String taskNote,
            LocalDateTime createTime,
            LocalDateTime updateTime) {

        this.id = id;
        this.title = title;
        this.completed = completed;
        this.registrationDate = registrationDate;
        this.registrationTime = registrationTime;
        this.taskContent = taskContent;
        this.taskProgress = taskProgress;
        this.taskNote = taskNote;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public LocalTime getRegistrationTime() {
        return registrationTime;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public int getTaskProgress() {
        return taskProgress;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
}