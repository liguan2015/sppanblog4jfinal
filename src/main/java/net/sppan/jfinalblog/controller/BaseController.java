package net.sppan.jfinalblog.controller;

import net.sppan.jfinalblog.model.User;
import net.sppan.jfinalblog.service.LoginService;

import com.jfinal.core.Controller;

public class BaseController extends Controller {
	private User loginAccount = null;

	public User getLoginAccount() {
		if (loginAccount == null) {
			loginAccount = getAttr(LoginService.loginUserCacheName);
			if (loginAccount != null && !loginAccount.isStatusOk()) {
				throw new IllegalStateException("当前用户状态不允许登录，status = " + loginAccount.getStatus());
			}
		}
		return loginAccount;
	}
}
