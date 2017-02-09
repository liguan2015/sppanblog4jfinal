package net.sppan.jfinalblog.controller.my;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.User;
import net.sppan.jfinalblog.service.UserService;

public class MyController extends BaseController {
	private final UserService service = UserService.me;
	public void index(){
		User user = service.findById(getLoginUser().getId());
		setAttr("user", user);
		render("index.html");
	}
}
