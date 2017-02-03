package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;

public class MyBlogController extends BaseController {

	public void index() {
		render("index.html");
	}
	
	public void add() {
		render("add.html");
	}
}
