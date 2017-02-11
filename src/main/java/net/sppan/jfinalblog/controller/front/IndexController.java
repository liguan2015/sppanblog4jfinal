package net.sppan.jfinalblog.controller.front;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.BlogService;
import net.sppan.jfinalblog.service.CategoryService;

public class IndexController extends BaseController {
	private final BlogService blogService = BlogService.me;
	private final CategoryService categoryService = CategoryService.me;

	public void index(){
		Integer categoryId = getParaToInt();
		//如果没有指定分类，则使用首页
		if(categoryId == null){
			categoryId = 0;
		}
		int pageNumber = getParaToInt("p",1);
		Page<Record> page = blogService.getPageNoContent(pageNumber,5,categoryId,false);
		setAttr("blogPage", page);
		setAttr("c", categoryService.findById(categoryId));
		render("index.html");
	}
}
