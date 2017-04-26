package net.sppan.blog.controller.front;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sppan.blog.service.BlogService;

public class TagsController extends Controller{
	private final BlogService blogService = BlogService.me;

	public void index(){
		int pageNumber = getParaToInt("p",1);
		String tagName = getPara();
		try {
			tagName = URLDecoder.decode(tagName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Page<Record> page = blogService.findPageNoContentTag(pageNumber, 5, tagName);
		setAttr("blogPage", page);
		setAttr("tagName", tagName);
		render("index.html");
	}
}
