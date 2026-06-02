package jp.ok.todoapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.ok.todoapp.entity.TodoEntity;
import jp.ok.todoapp.enums.Priority;
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
	/**
	 * 指定したユーザーIDのTodo一覧を登録日時の新しい順で取得する
	 */
	List<TodoEntity> findByUserIdOrderByCreatedAtDesc(int userId);
	
	/**
	 * 指定したユーザーIDと優先度に一致するTodo一覧を取得する
	 */
	List<TodoEntity> findByUserIdAndPriorityOrderByCreatedAtDesc(int userId, Priority priority);
	
	/**
	 * 指定したユーザーIDと状態に一致するTodo一覧を取得する
	 */
	List<TodoEntity> findByUserIdAndStateOrderByCreatedAtDesc(int userId, boolean state);
	
	/**
	 * Todo IDとユーザーIDを条件にTodo1件取得する
	 */
	Optional<TodoEntity> findByIdAndUserId(int id, int userId);
}
