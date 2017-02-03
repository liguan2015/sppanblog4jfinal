package net.sppan.jfinalblog.routes;

import net.sppan.jfinalblog.controller.front.BlogController;
import net.sppan.jfinalblog.controller.front.CategoryController;
import net.sppan.jfinalblog.controller.front.IndexController;
import net.sppan.jfinalblog.controller.front.LoginController;
import net.sppan.jfinalblog.controller.front.MyBlogController;
import net.sppan.jfinalblog.controller.front.MyController;
import net.sppan.jfinalblog.controller.front.MySettingController;
import net.sppan.jfinalblog.controller.front.UploadController;

import com.jfinal.config.Routes;

public class FrontRoutes extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/views");

		add("/", IndexController.class, "/front");
		add("/login", LoginController.class, "/front");
		add("/c", CategoryController.class, "/front/category");
		add("/b", BlogController.class, "/front/blog");
		add("/my", MyController.class, "/front/my");
		add("/my/setting", MySettingController.class, "/front/my/setting");
		add("/my/blog", MyBlogController.class, "/front/my/blog");
		
		add("/upload", UploadController.class);
	}

}
