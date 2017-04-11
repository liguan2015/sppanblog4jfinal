package net.sppan.blog.controller;

import com.jfinal.core.Controller;

import net.sppan.blog.model.User;
import net.sppan.blog.service.LoginService;

public class BaseController extends Controller {
	private User loginUser = null;

	public User getLoginUser() {
		if (loginUser == null) {
			loginUser = getAttr(LoginService.loginUserCacheName);
			if (loginUser != null && !loginUser.isStatusOk()) {
				throw new IllegalStateException("当前用户状态不允许登录，status = " + loginUser.getStatus());
			}
		}
		return loginUser;
	}
	
	public boolean isLogin() {
		return getLoginUser() != null;
	}

	public boolean notLogin() {
		return !isLogin();
	}
}
