package net.sppan.blog.controller.admin;

import net.sppan.blog.controller.BaseController;

public class AdminIndexCtroller extends BaseController {
	
	public void index(){
		render("index.html");
	}
	
	public void welcome(){
		render("welcome.html");
	}

}
