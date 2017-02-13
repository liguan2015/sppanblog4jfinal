package net.sppan.jfinalblog.intercepter;

import net.sppan.jfinalblog.model.User;
import net.sppan.jfinalblog.service.LoginService;
import net.sppan.jfinalblog.utils.CookieUtils;
import net.sppan.jfinalblog.utils.IpKit;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 从 cookie 中获取 sessionId，如果获取到则根据该值使用 LoginService
 * 得到登录的 User 对象 ---> loginUser，供后续的流程使用
 * 
 * 注意：将此拦截器设置为全局拦截器，所有 action 都需要
 */
public class SessionInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
        User loginUser = null;
		Controller c = inv.getController();
		String sessionId = CookieUtils.getSessionIdFromCookie(c.getRequest(), c.getResponse());
		if (sessionId != null) {
			loginUser = LoginService.me.getLoginAccountWithSessionId(sessionId);
			if (loginUser == null) {
				String loginIp = IpKit.getRealIp(c.getRequest());
				loginUser = LoginService.me.loginWithSessionId(sessionId, loginIp);
			}
			if (loginUser != null) {
				// 用户登录账号
				c.setAttr(LoginService.loginUserCacheName, loginUser);
				inv.invoke();
			} else {
				//cookie 登录未成功，证明该 cookie 已经没有用处，删之
				CookieUtils.removeSessionIdFromCookie(c.getResponse());
				c.redirect("/login");
			}
		}else{
			c.redirect("/login");
		}

	}
}



