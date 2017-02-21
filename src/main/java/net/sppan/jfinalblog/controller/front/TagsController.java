package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.service.BlogService;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class TagsController extends Controller{
	private final BlogService blogService = BlogService.me;

	public void index(){
		int pageNumber = getParaToInt("p",1);
		String tagName = getPara();
		Page<Record> page = blogService.findPageNoContentTag(pageNumber, 5, tagName);
		setAttr("blogPage", page);
		setAttr("tagName", tagName);
		render("index.html");
	}
}
