package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.LoginService;
import net.sppan.jfinalblog.utils.IpKit;
import net.sppan.jfinalblog.validator.LoginValidator;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.Ret;

public class LoginController extends BaseController{
	private final LoginService service = LoginService.me;
	
	public void index(){
		render("login.html");
	}
	
	@Before(LoginValidator.class)
	public void doLogin(){
		String username = getPara("username");
		String password = getPara("password");
		Boolean keepLogin = getParaToBoolean("keepLogin");
		String loginIp = IpKit.getRealIp(getRequest());
		Ret ret = service.login(username, password, keepLogin, loginIp);
		if (ret.isOk()) {
			String sessionId = ret.getStr(LoginService.sessionIdName);
			int maxAgeInSeconds = ret.getAs("maxAgeInSeconds");
			setCookie(LoginService.sessionIdName, sessionId, maxAgeInSeconds, true);
			setAttr(LoginService.loginUserCacheName, ret.get(LoginService.loginUserCacheName));

			ret.set("returnUrl", getPara("returnUrl", "/admin"));    // 如果 returnUrl 存在则跳过去，否则跳去首页
		}
		renderJson(ret);
	}
	
	/**
	 * 退出登录
	 */
	@Clear
	@ActionKey("/logout")
	public void logout() {
		service.logout(getCookie(LoginService.sessionIdName));
		removeCookie(LoginService.sessionIdName);
		redirect("/");
	}
}
