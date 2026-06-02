package jp.ok.todoapp.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import jp.ok.todoapp.entity.UserEntity;
import jp.ok.todoapp.repository.UserRepository;;

/**
 * ユーザー情報に関する処理を行うService
 */
@Service
public class UserService {
	
	// UserRepositoryを使用してDB操作を行う
	private UserRepository userRepository;
	
	// UserRepositoryをDIする
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * GoogleログインしたユーザーがDBに存在しなければ登録するメソッド
	 * @param oidcUser
	 */
	public void registerIfNotExists(OidcUser oidcUser) {
		
		// Googleログイン情報取得
		String googleSub = oidcUser.getSubject();
		String name = oidcUser.getAttribute("name");
		
		// 既に登録済みなら何もしない
		if (userRepository.findByGoogleSub(googleSub).isPresent()) {
			return;
		}
		
		// ユーザー情報を作成する
		UserEntity user = new UserEntity(googleSub, name);
		
		// usersテーブルにINSERTする
		userRepository.save(user);
	}

	/**
	 * google_subからusersテーブルのidを取得する
	 * @param googleSub
	 */
	public int getUserIdByGoogleSub(String googleSub) {
		
		// google_subでユーザー情報を検索する
		UserEntity user = userRepository.findByGoogleSub(googleSub)
				.orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。google_sub=" + googleSub));
		
		// usersテーブルのidを返す
		return user.getId();
	}

}
