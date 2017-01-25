package net.sppan.jfinalblog.service;

import java.util.List;

import net.sppan.jfinalblog.model.Blog;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class BlogService {

	public static final BlogService me = new BlogService();
	private final Blog blogDao = new Blog().dao();
	
	public Page<Record> getPageNoContent(Integer pageNumber, Integer pageSize, Integer categoryId) {
		String select = "SELECT b.id,u.nickName authorName,b.createAt,b.featured,c.name categoryName,c.id categoryId,b.privacy,b.status,b.summary,b.tags,b.title,b.views";
		if(categoryId != null && categoryId > 0){
			String sqlExceptSelect = "FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id WHERE b.category = ? ";
			return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect,categoryId);
		}else{
			String sqlExceptSelect = "FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id";
			return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect);
		}
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

	public Ret change(Integer id, String type) {
		try {
			String sql = "";
			switch (type) {
			case "privacy":
				sql = "UPDATE tb_blog SET privacy = IF(privacy = 0,1,0) WHERE id =?";
				break;
			case "featured":
				sql = "UPDATE tb_blog SET featured = IF(featured = 0,1,0) WHERE id =?";
				break;
			case "status":
				sql = "UPDATE tb_blog SET status = IF(status = 0,1,0) WHERE id =?";
				break;
			default:
				break;
			}
			Db.update(sql,id);
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg","操作成功");
	}

	public List<Record> findTopN(int n, String type) {
		try {
			String sql = "";
			switch (type) {
			case "views":
				sql = "SELECT id ,title FROM tb_blog ORDER BY views DESC LIMIT ?";
				break;
			case "news":
				sql = "SELECT id ,title FROM tb_blog ORDER BY createAt DESC LIMIT ?";
				break;
			case "featured":
				sql = "SELECT id ,title FROM tb_blog WHERE featured = 1 ORDER BY createAt DESC LIMIT ?";
				break;
			default:
				break;
			}
			List<Record> list = Db.find(sql, n);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}