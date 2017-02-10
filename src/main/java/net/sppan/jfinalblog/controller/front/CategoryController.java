package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.BlogService;
import net.sppan.jfinalblog.service.CategoryService;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class CategoryController extends BaseController{
	private final BlogService blogService = BlogService.me;
	private final CategoryService categoryService = CategoryService.me;
	public void index(){
		Integer categoryId = getParaToInt("c",0);
		int pageNumber = getParaToInt("p",1);
		Page<Record> page = blogService.getPageNoContent(pageNumber,5,categoryId,false);
		setAttr("blogPage", page);
		setAttr("c", categoryService.findById(categoryId));
		render("index.html");
	}
}
