package jp.ok.todoapp.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jp.ok.todoapp.service.UserService;

/**
 * Googleログイン成功後の処理を行うクラス
 */
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	private final UserService userService;
	
	/**
	 * UserServiceをDIする
	 * ログイン成功後の遷移先を/todoにする
	 */
	public OAuth2LoginSuccessHandler(UserService userService) {
		this.userService = userService;
		setDefaultTargetUrl("/todos");
	}
	
	/**
	 * Googleログイン成功後に呼ばれる処理
	 */
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
			) throws IOException, ServletException {
		// Googleログインしたユーザー情報を取得する
		OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
		
		// DBにユーザー情報が存在しなければ登録する
		userService.registerIfNotExists(oidcUser);
		
		// ログイン成功後、設定した遷移先へ移動する
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
