package repositories;

import models.Todo;
import play.db.Database;

import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class TodoRepository {

    private final Database database;

    @Inject
    public TodoRepository(Database database) {
        this.database = database;
    }

    public List<Todo> findAll() {

        List<Todo> todos = new ArrayList<>();

        String sql = """
            SELECT
                id,
                title,
                completed,
                registration_date,
                registration_time,
                task_content,
                task_progress,
                task_note,
                create_time,
                update_time
            FROM todos
            ORDER BY id
            """;

        try (Connection connection = database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Todo todo = new Todo(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getBoolean("completed"),

                    resultSet.getDate("registration_date") != null
                        ? resultSet.getDate("registration_date").toLocalDate()
                        : null,

                    resultSet.getTime("registration_time") != null
                        ? resultSet.getTime("registration_time").toLocalTime()
                        : null,

                    resultSet.getString("task_content"),
                    resultSet.getInt("task_progress"),
                    resultSet.getString("task_note"),

                    resultSet.getTimestamp("create_time").toLocalDateTime(),
                    resultSet.getTimestamp("update_time").toLocalDateTime()
                );

                todos.add(todo);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return todos;
    }

    public void create(
            LocalDate registrationDate,
            LocalTime registrationTime,
            String title,
            String taskContent,
            int taskProgress,
            String taskNote) {

        String sql = """
            INSERT INTO todos (
                registration_date,
                registration_time,
                title,
                task_content,
                task_progress,
                task_note,
                completed
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            if (registrationDate != null) {
                statement.setDate(
                    1,
                    java.sql.Date.valueOf(registrationDate)
                );
            } else {
                statement.setNull(
                    1,
                    java.sql.Types.DATE
                );
            }

            if (registrationTime != null) {
                statement.setTime(
                    2,
                    java.sql.Time.valueOf(registrationTime)
                );
            } else {
                statement.setNull(
                    2,
                    java.sql.Types.TIME
                );
            }

            statement.setString(3, title);
            statement.setString(4, taskContent);
            statement.setInt(5, taskProgress);
            statement.setString(6, taskNote);

            // 進捗100%なら完了扱い
            statement.setBoolean(7, taskProgress == 100);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void toggleCompleted(Long id) {

        String sql = """
            UPDATE todos
            SET completed = NOT completed
            WHERE id = ?
            """;

        try (Connection connection = database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {

        String sql = "DELETE FROM todos WHERE id = ?";

        try (Connection connection = database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTitle(Long id, String title) {

        String sql = "UPDATE todos SET title = ? WHERE id = ?";

        try (Connection connection = database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setLong(2, id);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(
            Long id,
            LocalDate registrationDate,
            LocalTime registrationTime,
            String title,
            String taskContent,
            int taskProgress,
            String taskNote) {

        String sql = """
            UPDATE todos
            SET
                registration_date = ?,
                registration_time = ?,
                title = ?,
                task_content = ?,
                task_progress = ?,
                task_note = ?,
                completed = ?
            WHERE id = ?
            """;

        try (Connection connection = database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            if (registrationDate != null) {
                statement.setDate(
                    1,
                    java.sql.Date.valueOf(registrationDate)
                );
            } else {
                statement.setNull(1, java.sql.Types.DATE);
            }

            if (registrationTime != null) {
                statement.setTime(
                    2,
                    java.sql.Time.valueOf(registrationTime)
                );
            } else {
                statement.setNull(2, java.sql.Types.TIME);
            }

            statement.setString(3, title);
            statement.setString(4, taskContent);
            statement.setInt(5, taskProgress);
            statement.setString(6, taskNote);

            // 進捗100%なら完了
            statement.setBoolean(7, taskProgress == 100);

            statement.setLong(8, id);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}