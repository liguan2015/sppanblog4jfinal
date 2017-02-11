package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.BlogService;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class SearchController extends BaseController{
	private final BlogService blogService = BlogService.me;
	
	public void index(){
		String keyWord = getPara("keyWord");
		int pageNumber = getParaToInt("p",1);
		Page<Record> page = blogService.getPageNoContentSearch(pageNumber,5,keyWord);
		//处理关键字高亮
		for (Record record : page.getList()) {
			String title = record.get("title");
			title = title.replaceAll(keyWord, "<font color='red'>"+keyWord+"</font>");
			record.set("title", title);
			
			String summary = record.get("summary");
			summary = summary.replaceAll(keyWord, "<font color='red'>"+keyWord+"</font>");
			record.set("summary", summary);
		}
		setAttr("blogPage", page);
		setAttr("keyWord", keyWord);
		render("index.html");
	}
}
