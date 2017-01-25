package net.sppan.jfinalblog.routes;

import net.sppan.jfinalblog.controller.admin.AdminIndexCtroller;
import net.sppan.jfinalblog.controller.admin.BlogController;
import net.sppan.jfinalblog.controller.admin.CategoryController;
import net.sppan.jfinalblog.controller.admin.TagController;
import net.sppan.jfinalblog.controller.admin.UserController;

import com.jfinal.config.Routes;

public class AdminRoutes extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/views/admin");
		
		add("/admin", AdminIndexCtroller.class, "/");
		add("/admin/user", UserController.class, "/user");
		add("/admin/category", CategoryController.class, "/category");
		add("/admin/tag", TagController.class, "/tag");
		add("/admin/blog", BlogController.class, "/blog");
	}

}
