package jp.ok.todoapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.ok.todoapp.entity.UserEntity;

/**
 * ユーザー情報のDB操作を行うRepository
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	/**
	 * google_subを条件にユーザー情報を1件取得する
	 * @param googleSub
	 * @return
	 */
	Optional<UserEntity> findByGoogleSub(String googleSub);
}
