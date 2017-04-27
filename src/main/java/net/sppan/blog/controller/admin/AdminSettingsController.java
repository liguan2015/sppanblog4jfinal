package net.sppan.blog.controller.admin;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import net.sppan.blog.controller.BaseController;
import net.sppan.blog.model.Options;
import net.sppan.blog.service.OptionsService;

public class AdminSettingsController extends BaseController{
	
	private OptionsService service = OptionsService.me;

	public void index(){
		render("index.html");
	}
	
	public void list(){
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		Page<Options> page = service.getPage(pageNumber,pageSize);
		renderJson(page);
	}
	
	public void form(){
		String key = getPara();
		if(key != null){
			Options options = service.findByOptionKey(key);
			setAttr("settings", options);
		}
		render("form.html");
	}
	
	public void save(){
		Options options = getModel(Options.class,"");
		Ret ret = service.updateByOptionKey(options);
		renderJson(ret);
	}
}
