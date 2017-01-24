package net.sppan.jfinalblog.service;

import com.jfinal.plugin.activerecord.Page;

import net.sppan.jfinalblog.model.User;

public class UserService {
	public static final UserService me = new UserService();
	private final User userDao = new User().dao();
	
	public Page<User> getPage(int pageNumber, int pageSize){
		return userDao.paginate(pageNumber, pageSize, "select *", "from tb_user");
	}

	public User findById(Integer id) {
		return userDao.findById(id);
	}

}
