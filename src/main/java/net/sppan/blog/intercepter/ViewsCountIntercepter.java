package net.sppan.blog.intercepter;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JMap;

import net.sppan.blog.service.BlogService;
import net.sppan.blog.utils.CookieKit;

/**
 * 访问量统计拦截器
 * <br>
 * 用于控制恶意刷新页面或者重复进入页面时访问量一直增加问题，
 * 
 * @author SPPan
 *
 */
public class ViewsCountIntercepter implements Interceptor {

	public static JMap viewList = JMap.create();
	
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		Integer blogId = controller.getParaToInt();
		String sessionId = CookieKit.getSessionIdFromCookie(controller.getRequest(), controller.getResponse());
		String viewKey = sessionId + blogId;
		long currentTimeMillis = System.currentTimeMillis();
		if(viewList.containsKey(viewKey)){
			Long oldTimeMillis = viewList.getLong(viewKey);
			if(currentTimeMillis - oldTimeMillis > 3 * 60 * 1000){ //3分钟
				BlogService.me.updateViewsCounts(blogId);
				viewList.set(viewKey, currentTimeMillis);
			}
		}else{
			BlogService.me.updateViewsCounts(blogId);
			viewList.set(viewKey, currentTimeMillis);
		}
		inv.invoke();
	}

}
