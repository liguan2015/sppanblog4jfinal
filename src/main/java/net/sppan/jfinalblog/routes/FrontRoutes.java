package net.sppan.jfinalblog.routes;

import net.sppan.jfinalblog.controller.front.BlogController;
import net.sppan.jfinalblog.controller.front.IndexController;
import net.sppan.jfinalblog.controller.front.SearchController;
import net.sppan.jfinalblog.controller.front.TagsController;

import com.jfinal.config.Routes;

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
