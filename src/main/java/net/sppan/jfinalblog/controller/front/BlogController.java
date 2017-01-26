package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.Blog;
import net.sppan.jfinalblog.service.BlogService;

public class BlogController extends BaseController{
	private final BlogService blogService = BlogService.me;
	
	public void index(){
		Integer blogId = getParaToInt();
		Blog blog = blogService.findFullById(blogId);
		setAttr("blog", blog);
		render("detail.html");
	}
}
