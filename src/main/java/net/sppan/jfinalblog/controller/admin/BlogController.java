package net.sppan.jfinalblog.controller.admin;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.BlogService;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class BlogController extends BaseController {
	
	private final BlogService service = BlogService.me;
	
	public void index(){
		render("index.html");
	}

	public void list(){
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		Page<Record> page = service.getPageNoContent(pageNumber,pageSize);
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
}
