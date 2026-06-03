package jp.ok.todoapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.ok.todoapp.entity.TodoEntity;
import jp.ok.todoapp.enums.Priority;
import jp.ok.todoapp.service.TodoService;
import jp.ok.todoapp.service.UserService;

/**
 * Todo画面に関する処理を行うController
 */
@Controller
public class TodoController {

    private final TodoService todoService;
    private final UserService userService;
    
    /**
     * TodoServiceとUserServiceをDIする
     */
    public TodoController(TodoService todoService,
    					  UserService userService) {
    	this.todoService = todoService;
    	this.userService = userService;
    }

	/**
	 * 未完了Todo一覧画面を表示する
	 */
	@GetMapping("/todos")
	public String index(Model model,
						@AuthenticationPrincipal OidcUser oidcUser) {
		
		// Googleログイン情報から表示画面用のユーザー名を取得する
		String userName = oidcUser.getAttribute("name");
		
		// Googleログインユーザーの一意IDを取得する
		String googleSub = oidcUser.getSubject();
		
		// google_subからusersテーブルのidを取得する
		int userId = userService.getUserIdByGoogleSub(googleSub);
		
		// 未完了タスクを取得する
		List<TodoEntity> todos = todoService.getTodoListByState(userId, false);

		// HTML側でuserNameとして使用できるようにする
		model.addAttribute("userName", userName);
		model.addAttribute("todos", todos);
		model.addAttribute("currentPage", "incomplete");
		
		// templates/view/todos.htmlを表示する
		return "view/todos";
	}
	
	/**
	 * 完了Todo一覧画面を表示する
	 */
	@GetMapping("/todos/completed")
	public String completed(Model model,
							@AuthenticationPrincipal OidcUser oidcUser) {
		
		// Googleログイン情報から表示画面用のユーザー名を取得する
		String userName = oidcUser.getAttribute("name");
		
		// Googleログインユーザーの一意IDを取得する
		String googleSub = oidcUser.getSubject();
		
		// google_subからusersテーブルのidを取得する
		int userId = userService.getUserIdByGoogleSub(googleSub);
		
		// 未完了タスクを取得する
		List<TodoEntity> todos = todoService.getTodoListByState(userId, true);

		// HTML側でuserNameとして使用できるようにする
		model.addAttribute("userName", userName);
		model.addAttribute("todos", todos);
		model.addAttribute("currentPage", "completed");
		
		// templates/view/todos.htmlを表示する
		return "view/todos";
	}
	
	/**
	 * 追加タスクをDBに登録する
	 */
	@PostMapping("/add")
	public String addTask(@RequestParam String title,
						  @RequestParam(required = false) String memo,
						  @RequestParam(required = false) String dueDate,
						  @RequestParam String priority,
						  @AuthenticationPrincipal OidcUser oidcUser) {
		
		// Googleログインユーザーの一意IDを取得する
		String googleSub = oidcUser.getSubject();
		
		// google_subからusersテーブルのidを取得する
		int userId = userService.getUserIdByGoogleSub(googleSub);
		
		// 期限が入力されている場合のみLocalDateに変換する
		LocalDate parsedDueDate = null;
		if (dueDate != null && !dueDate.isBlank()) {
			parsedDueDate = LocalDate.parse(dueDate);
		}
		
		// 文字列の優先度をEnumに変換する
		Priority parsedPriority = Priority.valueOf(priority);
		
		todoService.addTask(
				userId,
				title,
				memo,
				parsedDueDate,
				parsedPriority
		);
		
		// 登録後はTodo一覧画面へリダイレクトする
		return "redirect:/todos";
	}
	
	/**
	 * Todoの完了状態を更新する
	 */
	@PostMapping("/todos/{id}/state")
	@ResponseBody
	public void updateTaskState(@PathVariable("id") int id,
							    @RequestParam boolean state,
							    @AuthenticationPrincipal OidcUser oidcUser) {
		
		// Googleログインユーザーの一意IDを取得する
		String googleSub = oidcUser.getSubject();
		
		// google_subからusersテーブルのidを取得する
		int userId = userService.getUserIdByGoogleSub(googleSub);
		
		// Todoの完了状態を更新する
		todoService.updateTaskState(userId, id, state);
	}
	
	/**
	 * Todoを削除する
	 */
	@PostMapping("/todos/{id}/delete")
	@ResponseBody
	public void deleteTask(@PathVariable("id") int id,
						   @AuthenticationPrincipal OidcUser oidcUser) {
		
		// Googleログインユーザーの一意IDを取得する
		String googleSub = oidcUser.getSubject();
		
		// google_subからusersテーブルのidを取得する
		int userId = userService.getUserIdByGoogleSub(googleSub);
		
		// Todoを削除する
		todoService.deleteTask(userId, id);
	}
	
	/**
	 * Todoを編集する
	 */
	@PostMapping("/todos/{id}/edit")
	@ResponseBody
	public void editTask(@PathVariable("id") int id,
						 @RequestParam String title,
						 @RequestParam(required = false) String memo,
						 @RequestParam(required = false) String dueDate,
						 @RequestParam String priority,
						 @AuthenticationPrincipal OidcUser oidcUser) {
		
		// Googleログインユーザーの一意IDを取得する
		String googleSub = oidcUser.getSubject();
		
		// google_subからusersテーブルのidを取得する
		int userId = userService.getUserIdByGoogleSub(googleSub);
		
		// 期限が入力されている場合のみLocalDateに変換する
		LocalDate parsedDueDate = null;
		if (dueDate != null && !dueDate.isBlank()) {
			parsedDueDate = LocalDate.parse(dueDate);
		}
		
		// 文字列の優先度をEnumに変換する
		Priority parsedPriority = Priority.valueOf(priority);
		
		// Todoを編集する
		todoService.updateTask(userId, id, title, memo, parsedDueDate, parsedPriority);
	}
}