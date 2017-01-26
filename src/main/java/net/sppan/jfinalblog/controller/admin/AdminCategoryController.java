package net.sppan.jfinalblog.controller.admin;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.Category;
import net.sppan.jfinalblog.service.CategoryService;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

public class AdminCategoryController extends BaseController {
	
	private final CategoryService service = CategoryService.me;
	
	public void index(){
		render("index.html");
	}

	public void list(){
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		Page<Category> page = service.getPage(pageNumber,pageSize);
		renderJson(page);
	}
	
	public void form(){
		Integer id = getParaToInt();
		if(id != null){
			Category category = service.findById(id);
			setAttr("category", category);
		}
		render("form.html");
	}
	
	public void save(){
		Category category = getModel(Category.class,"");
		Ret ret = service.saveOrUpdate(category);
		renderJson(ret);
	}
	
	public void del(){
		Integer id = getParaToInt();
		Ret ret = service.deleteById(id);
		renderJson(ret);
	}
	
	public void changeStatus(){
		Integer id = getParaToInt();
		Ret ret = service.changeStatus(id);
		renderJson(ret);
	}
}
