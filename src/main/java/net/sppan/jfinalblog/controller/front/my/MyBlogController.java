package net.sppan.jfinalblog.controller.front.my;

import java.util.List;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.BlogService;

import com.jfinal.plugin.activerecord.Record;

public class MyBlogController extends BaseController {

	private final BlogService service = BlogService.me;

	public void index() {
		List<Record> list = service.pagingMyBlog(getLoginUser().getId());
		setAttr("blogList", list);
		render("index.html");
	}

	public void add() {
		render("add.html");
	}
}
