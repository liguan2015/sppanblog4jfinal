package net.sppan.blog.routes;

import com.jfinal.config.Routes;

import net.sppan.blog.controller.admin.AdminAboutMeController;
import net.sppan.blog.controller.admin.AdminBlogController;
import net.sppan.blog.controller.admin.AdminCategoryController;
import net.sppan.blog.controller.admin.AdminIndexCtroller;
import net.sppan.blog.controller.admin.AdminTagController;
import net.sppan.blog.controller.admin.AdminUserController;
import net.sppan.blog.controller.admin.AdminYoulianController;
import net.sppan.blog.controller.admin.LoginController;
import net.sppan.blog.controller.admin.UploadController;
import net.sppan.blog.intercepter.SessionInterceptor;

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
		add("/admin/youlian", AdminYoulianController.class, "/youlian");
		add("/admin/about", AdminAboutMeController.class, "/about");
		
		add("/login", LoginController.class, "/");
		

		add("/upload", UploadController.class);
	}

}
