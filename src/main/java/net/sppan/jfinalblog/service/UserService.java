package net.sppan.jfinalblog.service;

import java.util.Date;

import net.sppan.jfinalblog.model.User;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class UserService {
	public static final UserService me = new UserService();
	private final User userDao = new User().dao();
	
	public Page<User> getPage(int pageNumber, int pageSize){
		return userDao.paginate(pageNumber, pageSize, "SELECT *", "FROM tb_user");
	}

	public User findById(Integer id) {
		return userDao.findById(id);
	}

	public Ret saveOrUpdate(User user) {
		try {
			if(user.getId() != null){
				user.update();
			}else{
				String salt = user.getSalt();
				String hashedPass = HashKit.sha256(salt + "111111");
				user.setPassword(hashedPass);
				user.setCreateAt(new Date());
				user.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret deleteById(Integer id) {
		try {
			userDao.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}
	
	public void updateAccountAvatar(int accountId, String relativePathFileName) {
		Db.update("update tb_user set avatar=? where id=? limit 1", relativePathFileName, accountId);
	}
}
