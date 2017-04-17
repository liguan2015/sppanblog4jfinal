package net.sppan.blog.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.core.JFinal;
import com.jfinal.kit.StrKit;

/**
 * cookie相关工具类
 */
public final class CookieKit {

	private CookieKit() {}

	private static final String USER_COOKIE_KEY    = "jfinalBlogId";
	private static String USER_COOKIE_SECRET = "&#%!&*";

	/**
	 * 从cookies中获取sessionID
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getSessionIdFromCookie(HttpServletRequest request, HttpServletResponse response) {
		String cookieKey = USER_COOKIE_KEY;
		// 获取cookie信息
		String userCookie = getCookie(request, cookieKey);
		// 1.cookie为空，直接清除
		if (StrKit.isBlank(userCookie)) {
			removeCookie(response, cookieKey);
			return null;
		}
		// 2.解密cookie
		String cookieInfo = null;
		// cookie 私钥
		String secret = USER_COOKIE_SECRET;
		try {
			cookieInfo = AESUtils.decrypt(secret, userCookie);
		} catch (RuntimeException e) {
			// ignore
		}
		// 3.异常或解密问题，直接清除cookie信息
		if (StrKit.isBlank(cookieInfo)) {
			removeCookie(response, cookieKey);
			return null;
		}
		String[] userInfo = cookieInfo.split("~");
		// 4.规则不匹配
		if (userInfo.length != 4) {
			removeCookie(response, cookieKey);
			return null;
		}
		String sessionId   = userInfo[0];
		String oldTime  = userInfo[1];
		String maxAge   = userInfo[2];
		// 5.判定时间区间，超时的cookie清理掉
		long now  = System.currentTimeMillis();
		long time = Long.parseLong(oldTime) + (Long.parseLong(maxAge) * 1000);
		if (time <= now) {
			removeCookie(response, cookieKey);
			return null;
		}
		return sessionId;
	}

	/**
	 * 
	 * cookie设计为: des(私钥).encode(sessionId~time~maxAge~ip)
	 * 
	 * @param response 
	 * @param sessionId  用户sessionId
	 * @param remember   是否记住密码、此参数控制cookie的 maxAge，默认为120分钟（只在当前会话）<br>
	 *                   记住密码默认为3年
	 * @return void
	 */
	public static void setSessionId2Cookie(HttpServletResponse response, String sessionId,String loginIp, boolean... remember) {
		// 当前毫秒数
		long now = System.currentTimeMillis();
		// 超时时间，默认120秒
		int maxAge = 120 * 60;
		
		if (remember.length > 0 && remember[0]) {
			maxAge = 3 * 365 * 24 * 60 * 60;//3年
		}
		// 构造cookie
		StringBuilder cookieBuilder = new StringBuilder()
			.append(sessionId).append("~")
			.append(now).append("~")
			.append(maxAge).append("~")
			.append(loginIp);

		// cookie 私钥
		String secret = USER_COOKIE_SECRET;
		// 加密cookie
		String userCookie = AESUtils.encrypt(secret, cookieBuilder.toString());

		String cookieKey = USER_COOKIE_KEY;
		// 设置用户的cookie、 -1 维持成session的状态
		setCookie(response, cookieKey, userCookie, maxAge);
	}

	/**
	 * 读取cookie
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if(null != cookies){
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 清除 sessionId的cookie 
	 * @param response
	 * @param key
	 */
	public static void removeSessionIdFromCookie(HttpServletResponse response) {
		setCookie(response, USER_COOKIE_KEY, null, 0);
	}
	/**
	 * 清除 某个指定的cookie 
	 * @param response
	 * @param key
	 */
	public static void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAgeInSeconds
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		// 指定为httpOnly保证安全性
		int version = JFinal.me().getServletContext().getMajorVersion();
		if (version >= 3) {
			cookie.setHttpOnly(true);
		}
		response.addCookie(cookie);
	}
}
