package jp.ok.todoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jp.ok.todoapp.security.OAuth2LoginSuccessHandler;

/**
 * Spring Securityの設定クラス
 */
@Configuration
public class SecurityConfig {
	
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	/**
	 * Googleログイン成功後に実行するHandlerをDIする
	 */
	public SecurityConfig(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
		this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
	}
	
	/**
	 * 認証・認可の設定を行う
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		// .headers(headers -> headers.cacheControl(cache -> cache.disable()))
		
		// URLごとのアクセス権限を設定する
		.authorizeHttpRequests(auth -> auth
				
				// ログイン画面、CSS、JSは未ログインでもアクセス可能とする
				.requestMatchers("/login", "/css/**", "/js/**").permitAll()
				
				// 上記以外の画面はログイン必須にする
				.anyRequest().authenticated()
		)
		
		// Google OAuthログインの設定を行う
		.oauth2Login(ouath2 -> ouath2
				
				// 独自ログイン画面のURLを指定する)
				.loginPage("/login")
				
				// Googleログイン成功後の処理を指定する
				.successHandler(oAuth2LoginSuccessHandler)
		)
		
		// ログアウト設定
		.logout(logout -> logout
			// ログアウト処理を行うURL
			.logoutUrl("/logout")
			
			// ログアウト成功後の遷移先
			.logoutSuccessUrl("/login")
			
			// セッションを無効化する
			.invalidateHttpSession(true)
			
			// 認証情報を削除する
			.clearAuthentication(true)
			
			// Cookieを削除する
			.deleteCookies("JSESSIONID")
		);
		
		return http.build();
	}
}