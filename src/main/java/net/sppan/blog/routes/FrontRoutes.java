package net.sppan.blog.routes;

import com.jfinal.config.Routes;

import net.sppan.blog.controller.front.BlogController;
import net.sppan.blog.controller.front.IndexController;
import net.sppan.blog.controller.front.SearchController;
import net.sppan.blog.controller.front.TagsController;

public class FrontRoutes extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/views");

		add("/", IndexController.class, "/front");
		add("/b", BlogController.class, "/front/blog");
		add("/s", SearchController.class, "/front/search");
		add("/t", TagsController.class, "/front/tags");
	}

}
