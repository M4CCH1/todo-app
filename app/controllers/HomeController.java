package controllers;

import models.Todo;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.TodoRepository;

import jakarta.inject.Inject;
import java.util.List;

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

        String title = request.body()
                .asFormUrlEncoded()
                .getOrDefault("title", new String[]{""})[0];

        title = title.trim();

        if (title.isEmpty()) {
            return badRequest("Todoのタイトルを入力してください");
        }

        todoRepository.create(title);

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