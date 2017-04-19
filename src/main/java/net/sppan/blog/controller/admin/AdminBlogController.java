package net.sppan.blog.controller.admin;

import java.util.List;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sppan.blog.controller.BaseController;
import net.sppan.blog.model.Blog;
import net.sppan.blog.model.Category;
import net.sppan.blog.model.Options;
import net.sppan.blog.service.BlogService;
import net.sppan.blog.service.CategoryService;
import net.sppan.blog.service.OptionsService;
import net.sppan.blog.service.OptionsService.optionKeyEnum;

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
		Long id = getParaToLong();
		String type = getPara("type");
		Ret ret = service.change(id,type);
		renderJson(ret);
	}
	
	public void del(){
		Long id = getParaToLong();
		Ret ret = service.deleteById(id);
		renderJson(ret);
	}
	
	public void form(){
		Long id = getParaToLong();
		Blog blog;
		if(id != null){
			blog = service.findById(id);
		}else{
			blog = new Blog();
			Options options = OptionsService.me.findByOptionKey(optionKeyEnum.defaultEditor.name());
			blog.setEditor(options.getOptionValue());
		}
		setAttr("blog", blog);
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
