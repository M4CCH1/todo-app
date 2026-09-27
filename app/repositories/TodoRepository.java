package repositories;

import models.Todo;
import play.db.Database;

import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TodoRepository {

    private final Database database;

    @Inject
    public TodoRepository(Database database) {
        this.database = database;
    }

    public List<Todo> findAll() {

        List<Todo> todos = new ArrayList<>();

        String sql = "SELECT id, title, completed FROM todos ORDER BY id";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Todo todo = new Todo(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getBoolean("completed")
                );

                todos.add(todo);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return todos;
    }

    public void create(String title) {

        String sql = "INSERT INTO todos (title, completed) VALUES (?, FALSE)";

        try (Connection connection = database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
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
}