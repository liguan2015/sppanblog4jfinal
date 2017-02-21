package net.sppan.jfinalblog.controller.admin;

import java.util.List;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.Tag;
import net.sppan.jfinalblog.service.TagService;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

public class AdminTagController extends BaseController {
	
	private final TagService service = TagService.me;
	
	public void index(){
		render("index.html");
	}
	
	public void tags_name(){
		List<String> tagsList = service.findAllNameList();
		renderJson(tagsList);
	}

	public void list(){
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		Page<Tag> page = service.getPage(pageNumber,pageSize);
		renderJson(page);
	}
	
	public void form(){
		Integer id = getParaToInt();
		if(id != null){
			Tag tag = service.findById(id);
			setAttr("tag", tag);
		}
		render("form.html");
	}
	
	public void save(){
		Tag tag = getModel(Tag.class,"");
		tag.setCount(0);
		Ret ret = service.saveOrUpdate(tag);
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
