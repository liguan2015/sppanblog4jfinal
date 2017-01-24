package net.sppan.jfinalblog.controller.admin;

import net.sppan.jfinalblog.controller.BaseController;

public class AdminIndexCtroller extends BaseController {
	
	public void index(){
		render("index.html");
	}
	
	public void welcome(){
		render("welcome.html");
	}

}
