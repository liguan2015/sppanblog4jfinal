package net.sppan.jfinalblog.service;

import java.util.Date;

import net.sppan.jfinalblog.model.Session;
import net.sppan.jfinalblog.model.User;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;


public class LoginService {
	public static final LoginService me = new LoginService();
	private final User userDao = new User().dao();
	
	// 存放登录用户的 cacheName
	public static final String loginUserCacheName = "loginUser";

	// "jfinalId" 仅用于 cookie 名称，其它地方如 cache 中全部用的 "sessionId" 来做 key
	public static final String sessionIdName = "jfinalBlogId";
	
	public Ret login(String username, String password,Boolean keepLogin, String loginIp){
		username = username.toLowerCase().trim();
		password = password.trim();
		User loginUser = userDao.findFirst("SELECT * FROM tb_user WHERE userName=? LIMIT 1", username);
		if (loginUser == null) {
			return Ret.fail("msg", "用户名或密码不正确");
		}
		if (loginUser.isStatusLockId()) {
			return Ret.fail("msg", "账号已被锁定");
		}
		if (loginUser.isStatusReg()) {
			return Ret.fail("msg", "账号未激活，请先激活账号");
		}

		String salt = loginUser.getSalt();
		String hashedPass = HashKit.sha256(salt + password);
		// 未通过密码验证
		if (loginUser.getPassword().equals(hashedPass) == false) {
			return Ret.fail("msg", "用户名或密码不正确");
		}
		// 如果用户勾选保持登录，暂定过期时间为 3 年，否则为 120 分钟，单位为秒
		long liveSeconds =  keepLogin!= null && keepLogin ? 3 * 365 * 24 * 60 * 60 : 120 * 60;
		// expireAt 用于设置 session 的过期时间点，需要转换成毫秒
		long expireAt = System.currentTimeMillis() + (liveSeconds * 1000);
		// 保存登录 session 到数据库
		Session session = new Session();
		String sessionId = StrKit.getRandomUUID();
		session.setId(sessionId);
		session.setUserId(loginUser.getId());
		session.setExpireAt(expireAt);
		if ( ! session.save()) {
			return Ret.fail("msg", "保存 session 到数据库失败，请联系管理员");
		}

		loginUser.removeSensitiveInfo();                                 // 移除 password 与 salt 属性值
		loginUser.put("sessionId", sessionId);                          // 保存一份 sessionId 到 loginAccount 备用
		CacheKit.put(loginUserCacheName, sessionId, loginUser);

		createLoginLog(loginUser.getId(), loginIp);

		return Ret.ok(sessionIdName, sessionId).set(loginUserCacheName, loginUser);
	
	}
	
	/**
	 * 创建登录日志
	 */
	private void createLoginLog(Integer userId, String loginIp) {
		Record loginLog = new Record().set("userId", userId).set("ip", loginIp).set("loginAt", new Date());
		Db.save("tb_login_log", loginLog);
	}

	public User getLoginAccountWithSessionId(String sessionId) {
		return CacheKit.get(loginUserCacheName, sessionId);
	}

	public User loginWithSessionId(String sessionId, String loginIp) {
		Session session = Session.me.findById(sessionId);
		if (session == null) {      // session 不存在
			return null;
		}
		if (session.isExpired()) {  // session 已过期
			session.delete();		// 被动式删除过期数据，此外还需要定时线程来主动清除过期数据
			return null;
		}

		User loginUser = userDao.findById(session.getUserId());
		// 找到 loginAccount 并且 是正常状态 才允许登录
		if (loginUser != null && loginUser.isStatusOk()) {
			loginUser.removeSensitiveInfo();                                 // 移除 password 与 salt 属性值
			loginUser.put("sessionId", sessionId);                          // 保存一份 sessionId 到 loginAccount 备用
			CacheKit.put(loginUserCacheName, sessionId, loginUser);

			createLoginLog(loginUser.getId(), loginIp);
			return loginUser;
		}
		return null;
	}

	/**
	 * 退出登录
	 */
	public void logout(String sessionId) {
		if (sessionId != null) {
			CacheKit.remove(loginUserCacheName, sessionId);
			Session.me.deleteById(sessionId);
		}
	}
	
	/**
	 * 从数据库重新加载登录账户信息
	 */
	public void reloadLoginAccount(User loginAccountOld) {
		String sessionId = loginAccountOld.get("sessionId");
		User loginAccount = userDao.findFirst("select * from tb_user where id=? limit 1", loginAccountOld.getId());
		loginAccount.removeSensitiveInfo();               // 移除 password 与 salt 属性值
		loginAccount.put("sessionId", sessionId);        // 保存一份 sessionId 到 loginAccount 备用

		// 集群方式下，要做一通知其它节点的机制，让其它节点使用缓存更新后的数据，
		// 将来可能把 account 用 id : obj 的形式放缓存，更新缓存只需要 CacheKit.remove("account", id) 就可以了，
		// 其它节点发现数据不存在会自动去数据库读取，所以未来可能就是在 AccountService.getById(int id)的方法引入缓存就好
		// 所有用到 account 对象的地方都从这里去取
		CacheKit.put(loginUserCacheName, sessionId, loginAccount);
	}

	/**
	 * 更新用户密码
	 * @param loginUser 需要更新密码的用户
	 * @param oldpassword 旧密码
	 * @param password1 新密码
	 * @param password2 重复新密码
	 * @return
	 */
	public Ret updatePassword(User loginUser, String oldpassword,
			String password1, String password2) {
		User user = userDao.findById(loginUser.getId());
		if(user == null){
			return Ret.fail("msg", "用户不存在");
		}
		String sha256Oldpassword = HashKit.sha256(user.getSalt() + oldpassword);
		if(!sha256Oldpassword.equals(user.getPassword())){
			return Ret.fail("msg", "旧密码不正确");
		}
		if(!password1.equals(password2)){
			return Ret.fail("msg", "两次输入的密码不一样");
		}
		String sha256Password = HashKit.sha256(user.getSalt() + password1);
		try {
			Db.update("tb_user","id", new Record().set("id", user.getId()).set("password", sha256Password));
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg","操作成功");
	}
}
