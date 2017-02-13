package net.sppan.jfinalblog.controller.admin;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.LoginService;
import net.sppan.jfinalblog.utils.CookieUtils;
import net.sppan.jfinalblog.utils.IpKit;
import net.sppan.jfinalblog.validator.LoginValidator;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.Ret;

public class LoginController extends BaseController{
	private final LoginService service = LoginService.me;
	
	@Clear
	public void index(){
		render("login.html");
	}
	
	@Clear
	@Before(LoginValidator.class)
	public void doLogin(){
		String username = getPara("username");
		String password = getPara("password");
		Boolean keepLogin = getParaToBoolean("keepLogin");
		String loginIp = IpKit.getRealIp(getRequest());
		Ret ret = service.login(username, password, keepLogin, loginIp);
		if (ret.isOk()) {
			String sessionId = ret.getStr(LoginService.sessionIdName);
			
			CookieUtils.setSessionId2Cookie(getResponse(), sessionId, loginIp, keepLogin != null && keepLogin);
			
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
	
	@ActionKey("/update_form")
	public void update_form(){
		render("update_form.html");
	}
	
	@ActionKey("/updatePwd")
	public void updatePwd(){
		String oldpassword = getPara("oldpassword");
		String password1 = getPara("password1");
		String password2 = getPara("password2");
		Ret ret = service.updatePassword(getLoginUser(),oldpassword,password1,password2);
		if(ret != null && ret.isOk()){
			//密码修改完成后删除cookie记录 ，强制下线当前修改了密码的用户
			CookieUtils.removeSessionIdFromCookie(getResponse());
		}
		renderJson(ret);
	}
}
