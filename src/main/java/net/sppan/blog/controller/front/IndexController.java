package net.sppan.blog.controller.front;

import net.sppan.blog.controller.BaseController;

public class IndexController extends BaseController {

	public void index(){
		redirect("/b/1");
	}
	
	public void about(){
		render("about.html");
	}
}
