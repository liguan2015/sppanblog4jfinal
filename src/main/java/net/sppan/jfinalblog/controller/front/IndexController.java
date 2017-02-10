package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;

public class IndexController extends BaseController {

	public void index(){
		redirect("/c/0");
	}
	
}
