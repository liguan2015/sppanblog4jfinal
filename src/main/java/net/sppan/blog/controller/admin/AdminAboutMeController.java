package net.sppan.blog.controller.admin;

import com.jfinal.kit.Ret;

import net.sppan.blog.controller.BaseController;
import net.sppan.blog.service.OptionsService;

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
