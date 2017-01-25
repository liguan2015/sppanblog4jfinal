package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.BlogService;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class IndexController extends BaseController {
	
	private final BlogService blogService = BlogService.me;

	public void index(){
		Integer categoryId = getParaToInt("c",0);
		int pageNumber = getParaToInt("p",1);
		Page<Record> page = blogService.getPageNoContent(pageNumber,5,categoryId);
		setAttr("blogPage", page);
		setAttr("c", categoryId);
		render("index.html");
	}
	
	public void view(){
		Integer id = getParaToInt();
		render("view.html");
	}
	
}
