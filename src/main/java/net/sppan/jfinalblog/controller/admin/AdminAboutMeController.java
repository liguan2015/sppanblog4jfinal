package net.sppan.jfinalblog.controller.admin;

import com.jfinal.kit.Ret;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.OptionsService;

public class AdminAboutMeController extends BaseController {
	
	public void index(){
		render("index.html");
	}
	
	public void save(){
		String content = getPara("content");
		Ret ret = OptionsService.me.updateAboutMe(content);
		renderJson(ret);
	}
}
