package net.sppan.jfinalblog.controller.front;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.Category;
import net.sppan.jfinalblog.service.BlogService;
import net.sppan.jfinalblog.service.CategoryService;

public class IndexController extends BaseController {
	
	private final CategoryService categoryService = CategoryService.me;
	private final BlogService blogService = BlogService.me;

	public void index(){
		List<Category> list = categoryService.findAll();
		setAttr("list", list);
		
		Page<Record> page = blogService.getPageNoContent(1,10);
		setAttr("blogPage", page);
		render("index.html");
	}
}
