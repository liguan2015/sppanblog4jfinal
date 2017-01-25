package net.sppan.jfinalblog.intercepter;

import java.util.HashSet;
import java.util.Set;

import net.sppan.jfinalblog.model.User;
import net.sppan.jfinalblog.service.LoginService;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.PropKit;

/**
 * 后台权限管理拦截器
 * 
 * 暂时做成最简单的判断当前用户是否是管理员账号，后续改成完善的
 * 基于用户、角色、权限的权限管理系统，并且实现角色、权限完全动态化配置
 */
public class AdminAuthInterceptor implements Interceptor {

	private static Set<String> adminAccountSet = initAdmin();

	private static Set<String> initAdmin() {
		Set<String> ret = new HashSet<String>();
		String admin = PropKit.get("admin");        // 从配置文件中读取管理员账号，多个账号用逗号分隔
		String[] adminArray = admin.split(",");
		for (String a : adminArray) {
			ret.add(a.trim());
		}
		return ret;
	}

	public static boolean isAdmin(User loginUser) {
		return loginUser != null && adminAccountSet.contains(loginUser.getUserName());
	}

	public void intercept(Invocation inv) {
		User loginUser = inv.getController().getAttr(LoginService.loginUserCacheName);
		if (isAdmin(loginUser)) {
			inv.invoke();
		} else {
			inv.getController().renderError(404);
		}
	}
}

