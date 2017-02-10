package net.sppan.jfinalblog.service;

import java.util.Date;
import java.util.List;

import net.sppan.jfinalblog.model.Blog;
import net.sppan.jfinalblog.utils.HtmlFilter;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

public class BlogService {

	public static final BlogService me = new BlogService();
	public static final String blogCacheName = "blogCache";
	private final Blog blogDao = new Blog().dao();
	
	/**
	 * 分页查询博客信息
	 * @param pageNumber 当前页
	 * @param pageSize 每页条数
	 * @param categoryId 分类ID 如果为空，则查询所有分类
	 * @param isAdmin 是否是后台查询
	 * @return
	 */
	public Page<Record> getPageNoContent(Integer pageNumber, Integer pageSize, Integer categoryId,boolean isAdmin) {
		String select = "SELECT b.id,u.nickName authorName,u.avatar avatar,b.createAt,b.featured,c.name categoryName,c.id categoryId,b.privacy,b.status,b.summary,b.tags,b.title,b.views";
		String cacaheKey = String.format("GETPAGENOCONTENTFOR%dTO%dCATEGORY%dADMIN%s",pageNumber,pageSize,categoryId,String.valueOf(isAdmin));
		if(categoryId != null && categoryId > 0){
			StringBuffer sqlExceptSelect = new StringBuffer("FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id WHERE b.category = ? ");
			if(!isAdmin){
				sqlExceptSelect.append(" AND b.privacy = 0");
			}
			sqlExceptSelect.append(" ORDER BY b.createAt DESC");
			return Db.paginateByCache(blogCacheName,cacaheKey,pageNumber, pageSize, select, sqlExceptSelect.toString(),categoryId);
		}else{
			StringBuffer sqlExceptSelect = new StringBuffer("FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id");
			if(!isAdmin){
				sqlExceptSelect.append(" WHERE b.privacy = 0");
			}
			sqlExceptSelect.append(" ORDER BY b.createAt DESC");
			return Db.paginateByCache(blogCacheName,cacaheKey,pageNumber, pageSize, select, sqlExceptSelect.toString());
		}
	}

	/**
	 * 查询博客信息
	 * @param id 博客ID
	 * @return
	 */
	public Blog findById(final Integer id) {
		return CacheKit.get(blogCacheName, String.format("FINDBYIDFOR%d", id), new IDataLoader() {
			@Override
			public Blog load() {
				return blogDao.findById(id);
			}
		});
	}

	/**
	 * 保存或者更新博客
	 * @param blog
	 * @return
	 */
	public Ret saveOrUpdate(Blog blog) {
		try {
			if(blog.getId() != null){
				blog.update();
			}else{
				blog.setCreateAt(new Date());
				blog.setFeatured(0);
				blog.setStatus(0);
				blog.setViews(0);
				blog.setSummary(HtmlFilter.truncate(blog.getContent(),300));
				blog.save();
			}
			CacheKit.removeAll(blogCacheName);
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg",e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	/**
	 * 删除文章
	 * @param id 文章ID
	 * @return
	 */
	public Ret deleteById(Integer id) {
		try {
			blogDao.deleteById(id);
			CacheKit.removeAll(blogCacheName);
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	/**
	 * 更新博客的状态
	 * @param id 博客ID
	 * @param type 类型<br>
	 * 	privacy 权限 0公开 1私密<br>
	 *  featured 置顶 0正常 1置顶<br>
	 *  status 状态 0正常 1隐藏
	 * @return
	 */
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
			CacheKit.removeAll(blogCacheName);
			Db.update(sql,id);
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg","操作成功");
	}

	/**
	 * 查询topN博客信息
	 * @param n 
	 * @param type 类型<br>
	 * views 浏览量 news 最新发布  featured 置顶
	 * 
	 * @return
	 */
	public List<Record> findTopN(int n, String type) {
		try {
			String sql = "";
			switch (type) {
			case "views":
				sql = "SELECT id ,title,views FROM tb_blog ORDER BY views DESC LIMIT ?";
				break;
			case "news":
				sql = "SELECT id ,title,views FROM tb_blog ORDER BY createAt DESC LIMIT ?";
				break;
			case "featured":
				sql = "SELECT id ,title,views FROM tb_blog WHERE featured = 1 ORDER BY createAt DESC LIMIT ?";
				break;
			default:
				break;
			}
			List<Record> list = Db.findByCache(blogCacheName, String.format("FINDTOPN%dTYPE%s", n,type), sql, n);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询博客的完整信息
	 * @param blogId 博客ID
	 * @return
	 */
	public Blog findFullById(Integer blogId) {
		String sql = "SELECT b.*,u.avatar authorAvatar,u.nickName authorName,c.name categoryName FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id WHERE b.id = ?";
		Blog blog = blogDao.findFirstByCache(blogCacheName,String.format("FINDFULLBYIDFOR%d", blogId),sql,blogId);
		//系统缓存都是设置了30分钟失效，缓存失效后自动重新加载数据库信息，所有浏览量会延迟30分钟
		Db.update("UPDATE tb_blog SET views = views + 1");
		return blog;
	}
}
