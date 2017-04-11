package net.sppan.blog.controller.front;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import net.sppan.blog.controller.BaseController;
import net.sppan.blog.lucene.SearcherBean;
import net.sppan.blog.lucene.SearcherKit;

public class SearchController extends BaseController{
	
	public void index(){
		String keyWord = getPara("keyword");
		if(StrKit.isBlank(keyWord)){
			redirect("/");
			return;
		}
		int pageNumber = getParaToInt("p",1);
		Page<SearcherBean> search = SearcherKit.search(pageNumber, 5, keyWord);
		setAttr("keyWord", keyWord);
		setAttr("blogPage", search);
		render("index.html");
	}
}
