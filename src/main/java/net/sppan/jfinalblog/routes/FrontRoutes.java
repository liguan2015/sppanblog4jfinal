package net.sppan.jfinalblog.routes;

import net.sppan.jfinalblog.controller.front.IndexController;

import com.jfinal.config.Routes;

public class FrontRoutes extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/views");
		
		add("/", IndexController.class);
	}

}
