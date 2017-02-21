package net.sppan.jfinalblog.controller.front;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.Blog;
import net.sppan.jfinalblog.model.Category;
import net.sppan.jfinalblog.service.BlogService;
import net.sppan.jfinalblog.service.CategoryService;

public class BlogController extends BaseController{
	private final BlogService blogService = BlogService.me;
	private final CategoryService categoryService = CategoryService.me;
	
	public void index(){
		Integer categoryId = getParaToInt() == null ? 0 :getParaToInt();
		int pageNumber = getParaToInt("p",1);
		Page<Record> page = blogService.findPageNoContent(pageNumber,5,categoryId,false);
		setAttr("blogPage", page);
		setAttr("c", categoryId);
		render("index.html");
	}
	
	public void view(){
		Integer blogId = getParaToInt();
		Blog blog = blogService.findFullById(blogId);
		setAttr("blog", blog);
		
		Category category = categoryService.findById(blog.getCategory());
		setAttr("category", category);
		render("detail.html");
	}
}
