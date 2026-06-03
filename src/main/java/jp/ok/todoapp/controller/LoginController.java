package jp.ok.todoapp.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン画面に関する処理を行うController
 */
@Controller
public class LoginController {

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
    	
    	// 既にログイン済みの場合はTodo一覧画面へリダイレクトする
    	if (authentication != null && authentication.isAuthenticated()) {
    		return "redirect:/todos";
    	}
    	
    	// 未ログインの場合はログイン画面を表示する
        return "view/login";
    }
}