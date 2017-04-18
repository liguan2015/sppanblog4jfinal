package net.sppan.blog.controller.front;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sppan.blog.controller.BaseController;
import net.sppan.blog.intercepter.ViewsCountIntercepter;
import net.sppan.blog.model.Blog;
import net.sppan.blog.model.Category;
import net.sppan.blog.service.BlogService;
import net.sppan.blog.service.CategoryService;

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
	
	@Before(ViewsCountIntercepter.class)
	public void view(){
		Long blogId = getParaToLong();
		Blog blog = blogService.findFullById(blogId);
		setAttr("blog", blog);
		
		Category category = categoryService.findById(blog.getCategory());
		setAttr("category", category);
		render("detail.html");
	}
}
