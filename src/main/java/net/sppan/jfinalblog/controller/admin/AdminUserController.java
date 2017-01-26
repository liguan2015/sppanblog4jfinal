package net.sppan.jfinalblog.controller.admin;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.User;
import net.sppan.jfinalblog.service.UserService;

public class AdminUserController extends BaseController{
	private final UserService service = UserService.me;

	public void index(){
		render("index.html");
	}
	
	public void list(){
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		Page<User> page = service.getPage(pageNumber,pageSize);
		renderJson(page);
	}
	
	public void form(){
		Integer id = getParaToInt();
		if(id != null){
			User user = service.findById(id);
			setAttr("user", user);
		}
		render("form.html");
	}
	
	public void save(){
		User user = getModel(User.class,"");
		Ret ret = service.saveOrUpdate(user);
		renderJson(ret);
	}
	
	public void del(){
		Integer id = getParaToInt();
		Ret ret = service.deleteById(id);
		renderJson(ret);
	}
}
