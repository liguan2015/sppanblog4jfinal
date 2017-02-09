package net.sppan.jfinalblog.routes;

import net.sppan.jfinalblog.controller.my.MyController;
import net.sppan.jfinalblog.controller.my.MySettingController;

import com.jfinal.config.Routes;

public class MyRoutes extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/views");

		add("/my", MyController.class, "/my");
		add("/my/setting", MySettingController.class, "/my/setting");
	}

}
