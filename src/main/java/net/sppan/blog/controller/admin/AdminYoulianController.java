package net.sppan.blog.controller.admin;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import net.sppan.blog.controller.BaseController;
import net.sppan.blog.model.Youlian;
import net.sppan.blog.service.YoulianService;

public class AdminYoulianController extends BaseController{
	
	private YoulianService service = YoulianService.me;

	public void index(){
		render("index.html");
	}
	
	public void list(){
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		Page<Youlian> page = service.getPage(pageNumber,pageSize);
		renderJson(page);
	}
	
	public void form(){
		Integer id = getParaToInt();
		if(id != null){
			Youlian youlian = service.findById(id);
			setAttr("youlian", youlian);
		}
		render("form.html");
	}
	
	public void save(){
		Youlian youlian = getModel(Youlian.class,"");
		Ret ret = service.saveOrUpdate(youlian);
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
