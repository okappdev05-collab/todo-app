package jp.ok.todoapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jp.ok.todoapp.enums.Priority;

@Entity
@Table(name = "todos")
public class TodoEntity {
	
	// Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// usersテーブルのid
	@Column(name = "user_id", nullable = false)
	private int userId;
	
	// タスクのタイトル
	@Column(name = "title", nullable = false)
	private String title;
	
	// タスクの内容
	@Column(name = "memo")
	private String memo;
	
	// タスクの期限
	@Column(name = "due_date")
	private LocalDate dueDate;
	
	// タスクの優先度
	@Enumerated(EnumType.STRING)
	@Column(name = "priority", nullable = false)
	private Priority priority;
	
	// タスクの状態
	@Column(name = "state", nullable = false)
	private boolean state;
	
	// 登録日時
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	// 更新日時
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	// デフォルトコンストラクタ
	protected TodoEntity() {
	}
	
	// コンストラクタ
	public TodoEntity(
			int userId,
			String title,
			String memo,
			LocalDate dueDate,
			Priority priority,
			boolean state) {
		this.userId = userId;
		this.title = title;
		this.memo = memo;
		this.dueDate = dueDate;
		this.priority = priority;
		this.state = state;
	}
	
	// Getter[ID]
	public int getId() {
		return id;
	}
	
	// Getter[UserId]
	public int getUserId() {
		return userId;
	}
	
	// Setter[UserId]
	public void setUserId(int userId) {
		this.userId = userId;
	}

	// Getter[title]
	public String getTitle() {
		return title;
	}
	
	// Setter[title]
	public void setTitle(String title) {
		this.title = title;
	}
	
	// Getter[memo]
	public String getMemo() {
		return memo;
	}
	
	// Setter[memo]
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	// Getter[dueDate]
	public LocalDate getDueDate() {
		return dueDate;
	}
	
	// Setter[dueDate]
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	
	// Getter[priority]
	public Priority getPriority() {
		return priority;
	}
	
	// Setter[priority]
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	// Getter[state]
	public boolean getState() {
		return state;
	}
	
	// Setter[state]
	public void setState(boolean state) {
		this.state = state;
	}
	
	// Getter[createdAt]
	public LocalDateTime getCreatedAt() {
	    return createdAt;
	}

	// Getter[updatedAt]
	public LocalDateTime getUpdatedAt() {
	    return updatedAt;
	}
}
