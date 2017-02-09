package net.sppan.jfinalblog.intercepter;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 */
public class LoginInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		inv.invoke();
	}
}



