package net.sppan.jfinalblog.routes;

import net.sppan.jfinalblog.controller.admin.AdminIndexCtroller;
import net.sppan.jfinalblog.controller.admin.UserController;

import com.jfinal.config.Routes;

public class AdminRoutes extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/views/admin");
		
		add("/admin", AdminIndexCtroller.class, "/");
		add("/admin/user", UserController.class, "/user");
	}

}
