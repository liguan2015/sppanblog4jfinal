package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.lucene.SearcherBean;
import net.sppan.jfinalblog.lucene.SearcherKit;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

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
