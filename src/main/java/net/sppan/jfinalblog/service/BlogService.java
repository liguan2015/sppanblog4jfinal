package net.sppan.jfinalblog.service;

import net.sppan.jfinalblog.model.Blog;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class BlogService {

	public static final BlogService me = new BlogService();
	private final Blog blogDao = new Blog().dao();
	
	public Page<Record> getPageForAdmin(Integer pageNumber, Integer pageSize) {
		String select = "SELECT b.id,u.nickName authorName,b.createAt,b.featured,c.name categoryName,b.privacy,b.status,b.summary,b.tags,b.title,b.views";
		String sqlExceptSelect = "FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id";
		return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect);
	}

	public Blog findById(Integer id) {
		return blogDao.findById(id);
	}

	public Ret saveOrUpdate(Blog blog) {
		try {
			if(blog.getId() != null){
				blog.update();
			}else{
				blog.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg",e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret deleteById(Integer id) {
		try {
			blogDao.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}
	
}
