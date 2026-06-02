package jp.ok.todoapp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.ok.todoapp.entity.TodoEntity;
import jp.ok.todoapp.enums.Priority;
import jp.ok.todoapp.repository.TodoRepository;

@Service
public class TodoService {

	// TodoRepositoryを使用してDB操作を行う
	private TodoRepository todoRepository;
	
	public TodoService(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}
	
	// 追加タスクをDBに登録するメソッド
	public void addTask(
			int userId,
			String title,
			String memo,
			LocalDate dueDate,
			Priority priority) {
		// 追加時は状態を未着手で設定する
		boolean state = false;
		
		TodoEntity todo = new TodoEntity(
				userId,
				title,
				memo,
				dueDate,
				priority,
				state);
		
		// todosテーブルにINSERTする
		todoRepository.save(todo);
	}
	
	/**
	 * 指定したユーザーIDのTodo一覧を取得する
	 */
	public List<TodoEntity> getTodoList(int userId) {
		
		// 指定したユーザーのTodo一覧を登録日時の新しい順で取得する
		return todoRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}
	
	/**
	 * Todoの完了状態を更新する
	 */
	public void updateTaskState(int userId, int todoId, boolean state) {
		// Todo IDで対象タスクを取得する
		TodoEntity todo = todoRepository.findByIdAndUserId(todoId, userId)
				.orElseThrow(() -> new IllegalArgumentException("タスクが存在しない、またはログイン中ユーザーのタスクではありません。 id = " + todoId));
		
		// 完了状態を更新する
		todo.setState(state);
		
		// todosテーブルをUPDATEする
		todoRepository.save(todo);
	}
	
	/**
	 * Todoのタスクを更新する
	 */
	public void updateTask(int userId, int todoId, String title, String memo, LocalDate dueDate, Priority priority) {
		// Todo IDで対象タスクを取得する
		TodoEntity todo = todoRepository.findByIdAndUserId(todoId, userId)
				.orElseThrow(() -> new IllegalArgumentException("タスクが存在しない、またはログイン中ユーザーのタスクではありません。 id = " + todoId));
		
		// タスクを更新する
		todo.setTitle(title);
		todo.setMemo(memo);
		todo.setDueDate(dueDate);
		todo.setPriority(priority);
		
		// todosテーブルをUPDATEする
		todoRepository.save(todo);
	}
	
	/**
	 * Todoを削除する
	 */
	public void deleteTask(int userId, int todoId) {
		TodoEntity todo = todoRepository.findByIdAndUserId(todoId, userId)
				.orElseThrow(() -> new IllegalArgumentException("タスクが存在しない、またはログイン中ユーザーのタスクではありません。 id = " + todoId));
		
		// 指定IDのTodoを削除する
		todoRepository.delete(todo);
	}
}
