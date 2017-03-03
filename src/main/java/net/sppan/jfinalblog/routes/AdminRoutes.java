package net.sppan.jfinalblog.routes;

import net.sppan.jfinalblog.controller.admin.AdminAboutMeController;
import net.sppan.jfinalblog.controller.admin.AdminBlogController;
import net.sppan.jfinalblog.controller.admin.AdminCategoryController;
import net.sppan.jfinalblog.controller.admin.AdminIndexCtroller;
import net.sppan.jfinalblog.controller.admin.AdminTagController;
import net.sppan.jfinalblog.controller.admin.AdminUserController;
import net.sppan.jfinalblog.controller.admin.LoginController;
import net.sppan.jfinalblog.controller.admin.UploadController;
import net.sppan.jfinalblog.intercepter.SessionInterceptor;

import com.jfinal.config.Routes;

public class AdminRoutes extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/views/admin");
		addInterceptor(new SessionInterceptor());
		
		add("/admin", AdminIndexCtroller.class, "/");
		add("/admin/user", AdminUserController.class, "/user");
		add("/admin/category", AdminCategoryController.class, "/category");
		add("/admin/tag", AdminTagController.class, "/tag");
		add("/admin/blog", AdminBlogController.class, "/blog");
		add("/admin/about", AdminAboutMeController.class, "/about");
		
		add("/login", LoginController.class, "/");
		

		add("/upload", UploadController.class);
	}

}
