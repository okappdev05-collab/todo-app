package jp.ok.todoapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "users")
public class UserEntity {
	
	// Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// Google一意のトークン
	@Column(name = "google_sub", nullable = false, unique = true )
	private String googleSub;
	
	// 名前
	@Column(name = "name", nullable = false)
	private String name;
	
	// 登録日時
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	// デフォルトコンストラクタ
	protected UserEntity() {
		
	}
	
	public UserEntity(
			String googleSub,
			String name
			) {
		this.googleSub = googleSub;
		this.name = name;
	}
	
	// Getter[ID]
	public int getId() {
		return id;
	}
	
	// Getter, Setter[Google一意のトークン]
	public String getGoogleSub() {
		return googleSub;
	}
	
	public void setGoogleSub(String googleSub) {
		this.googleSub = googleSub;
	}
	
	// Getter, Setter[名前]
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	// Getter[登録日時]
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
