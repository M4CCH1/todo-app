package controllers;

import models.Todo;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.TodoRepository;

import jakarta.inject.Inject;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class HomeController extends Controller {

    private final TodoRepository todoRepository;

    @Inject
    public HomeController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Result index() {

        List<Todo> todos = todoRepository.findAll();

        return ok(views.html.index.render(todos));
    }

    public Result create(Http.Request request) {

        var form = request.body().asFormUrlEncoded();

        String registrationDateText =
                form.getOrDefault("registrationDate", new String[]{""})[0].trim();

        String registrationTimeText =
                form.getOrDefault("registrationTime", new String[]{""})[0].trim();

        String title =
                form.getOrDefault("title", new String[]{""})[0].trim();

        String taskContent =
                form.getOrDefault("taskContent", new String[]{""})[0].trim();

        String taskProgressText =
                form.getOrDefault("taskProgress", new String[]{"0"})[0].trim();

        String taskNote =
                form.getOrDefault("taskNote", new String[]{""})[0].trim();

        // タスク名チェック
        if (title.isEmpty()) {
            return badRequest("タスク名を入力してください");
        }

        if (title.length() > 15) {
            return badRequest("タスク名は15文字以内で入力してください");
        }

        // 内容チェック
        if (taskContent.isEmpty()) {
            return badRequest("内容を入力してください");
        }

        if (taskContent.length() > 30) {
            return badRequest("内容は30文字以内で入力してください");
        }

        // 備考チェック
        if (taskNote.length() > 30) {
            return badRequest("備考は30文字以内で入力してください");
        }

        // 日付
        LocalDate registrationDate = null;

        if (!registrationDateText.isEmpty()) {
            registrationDate = LocalDate.parse(registrationDateText);
        }

        // 時間
        LocalTime registrationTime = null;

        if (!registrationTimeText.isEmpty()) {

            if (registrationDate == null) {
                return badRequest("時間を入力する場合は年月日も入力してください");
            }

            registrationTime = LocalTime.parse(registrationTimeText);
        }

        // 進捗
        int taskProgress;

        try {
            taskProgress = Integer.parseInt(taskProgressText);
        } catch (NumberFormatException e) {
            return badRequest("進捗は0～100の整数で入力してください");
        }

        if (taskProgress < 0 || taskProgress > 100) {
            return badRequest("進捗は0～100で入力してください");
        }

        todoRepository.create(
                registrationDate,
                registrationTime,
                title,
                taskContent,
                taskProgress,
                taskNote
        );

        return redirect(routes.HomeController.index());
    }

    public Result toggleCompleted(Long id) {

        todoRepository.toggleCompleted(id);

        return redirect(routes.HomeController.index());
    }

    public Result delete(Long id) {

        todoRepository.delete(id);

        return redirect(routes.HomeController.index());
    }

    public Result updateTitle(Long id, Http.Request request) {

        String title = request.body()
                .asFormUrlEncoded()
                .getOrDefault("title", new String[]{""})[0];

        title = title.trim();

        if (title.isEmpty()) {
            return badRequest("Todoのタイトルを入力してください");
        }

        todoRepository.updateTitle(id, title);

        return redirect(routes.HomeController.index());
    }
}