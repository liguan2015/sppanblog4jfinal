package net.sppan.jfinalblog.controller.admin;

import java.util.List;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.Blog;
import net.sppan.jfinalblog.model.Category;
import net.sppan.jfinalblog.service.BlogService;
import net.sppan.jfinalblog.service.CategoryService;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class AdminBlogController extends BaseController {
	
	private final BlogService service = BlogService.me;
	private final CategoryService categoryService = CategoryService.me;
	public void index(){
		render("index.html");
	}

	public void list(){
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		Page<Record> page = service.findPageNoContent(pageNumber,pageSize,null,true);
		renderJson(page);
	}
	
	public void change(){
		Integer id = getParaToInt();
		String type = getPara("type");
		Ret ret = service.change(id,type);
		renderJson(ret);
	}
	
	public void del(){
		Integer id = getParaToInt();
		Ret ret = service.deleteById(id);
		renderJson(ret);
	}
	
	public void form(){
		Integer id = getParaToInt();
		if(id != null){
			Blog blog = service.findById(id);
			setAttr("blog", blog);
		}
		List<Category> categories = categoryService.findAll();
		setAttr("categories", categories);
		render("form.html");
	}
	
	public void save(){
		Blog blog = getModel(Blog.class,"");
		blog.setAuthorId(Long.valueOf(getLoginUser().getId()));
		Ret ret = service.saveOrUpdate(blog);
		renderJson(ret);
	}
}
