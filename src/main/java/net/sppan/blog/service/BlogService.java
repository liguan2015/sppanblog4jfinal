package net.sppan.blog.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import net.sppan.blog.common.Constant;
import net.sppan.blog.lucene.SearcherBean;
import net.sppan.blog.lucene.SearcherKit;
import net.sppan.blog.model.Blog;
import net.sppan.blog.utils.HtmlFilterKit;
import net.sppan.blog.utils.MarkdownKit;

public class BlogService {

	public static final BlogService me = new BlogService();
	public static final String blogCacheName = "blogCache";
	private final TagService tagService = TagService.me;
	private final Blog blogDao = new Blog().dao();
	/**
	 * 分页查询博客信息
	 * @param pageNumber 当前页
	 * @param pageSize 每页条数
	 * @param categoryId 分类ID 如果为空，则查询所有分类
	 * @param isAdmin 是否是后台查询
	 * @return
	 */
	public Page<Record> findPageNoContent(Integer pageNumber, Integer pageSize, Integer categoryId,boolean isAdmin) {
		String select = "SELECT b.id,u.nickName authorName,u.avatar avatar,b.createAt,b.featured,c.name categoryName,c.id categoryId,b.privacy,b.status,b.summary,b.tags,b.title,b.views";
		String cacaheKey = String.format("GETPAGENOCONTENTFOR%dTO%dCATEGORY%dADMIN%s",pageNumber,pageSize,categoryId,String.valueOf(isAdmin));
		StringBuffer sqlExceptSelect = new StringBuffer("FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id");
		List<Object> params = new ArrayList<Object>(); 
		if(!isAdmin){
			sqlExceptSelect.append(" WHERE b.privacy = 0");
		}
		if(categoryId != null && categoryId > 0){
			sqlExceptSelect.append(" AND b.category = ? ");
			params.add(categoryId);
		}
		sqlExceptSelect.append(" ORDER BY b.createAt DESC");
		return Db.paginateByCache(blogCacheName,cacaheKey,pageNumber, pageSize, select, sqlExceptSelect.toString(),params.toArray());
	}

	/**
	 * 关键字 分页查询博客信息
	 * @param pageNumber 当前页
	 * @param pageSize 每页条数
	 * @param keyWord 关键字
	 * @return
	 */
	public Page<Record> findPageNoContentSearch(int pageNumber, int pageSize, String keyWord) {
		String select = "SELECT b.id,u.nickName authorName,u.avatar avatar,b.createAt,b.featured,c.name categoryName,c.id categoryId,b.privacy,b.status,b.summary,b.tags,b.title,b.views";
		String cacaheKey = String.format("FINDPAGENOCONTENTSEARCH%dTO%dCATEGORY%s",pageNumber,pageSize,keyWord);
		StringBuffer sqlExceptSelect = new StringBuffer("FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id WHERE b.privacy = 0 ");
		sqlExceptSelect.append(" AND (b.content LIKE ? OR b.title LIKE ? or b.summary LIKE ?) ");
		sqlExceptSelect.append(" ORDER BY b.createAt DESC");
		keyWord = "%" + keyWord + "%";
		return Db.paginateByCache(blogCacheName,cacaheKey,pageNumber, pageSize, select, sqlExceptSelect.toString(),keyWord, keyWord, keyWord);
	}
	
	/**
	 * 标签 分页查询博客信息
	 * @param pageNumber 当前页
	 * @param pageSize 每页条数
	 * @param tagName 标签名
	 * @return
	 */
	public Page<Record> findPageNoContentTag(int pageNumber, int pageSize, String tagName) {
		String select = "SELECT b.id,u.nickName authorName,u.avatar avatar,b.createAt,b.featured,c.name categoryName,c.id categoryId,b.privacy,b.status,b.summary,b.tags,b.title,b.views";
		String cacaheKey = String.format("FINDPAGENOCONTENTTAG%dTO%dCATEGORY%s",pageNumber,pageSize,tagName);
		StringBuffer sqlExceptSelect = new StringBuffer("FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id WHERE b.privacy = 0 ");
		sqlExceptSelect.append(" AND b.tags LIKE ? ");
		sqlExceptSelect.append(" ORDER BY b.createAt DESC");
		return Db.paginateByCache(blogCacheName,cacaheKey,pageNumber, pageSize, select, sqlExceptSelect.toString(),"%" + tagName + "%");
	}
	
	/**
	 * 查询博客信息
	 * @param id 博客ID
	 * @return
	 */
	public Blog findById(final Long id) {
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
			SearcherBean searcherBean;
			if(blog.getId() != null){
				searcherBean = new SearcherBean();
				
				if(Constant.editorType.markdown.name().equals(blog.getEditor())){
					String htmlContent = MarkdownKit.pegDown(blog.getContent());
					blog.setSummary(HtmlFilterKit.truncate(htmlContent,300));
					searcherBean.setContent(htmlContent);
				}else{
					blog.setSummary(HtmlFilterKit.truncate(blog.getContent(),300));
					searcherBean.setContent(blog.getContent());
				}
				blog.update();
				searcherBean.setId(String.valueOf(blog.getId()));
				searcherBean.setTitle(blog.getTitle());
				searcherBean.setSummary(blog.getSummary());
				SearcherKit.update(searcherBean);
			}else{
				//设置博客基本属性
				blog.setCreateAt(new Date());
				blog.setFeatured(0);
				blog.setStatus(0);
				blog.setViews(0);
				searcherBean = new SearcherBean();
				
				if(Constant.editorType.markdown.name().equals(blog.getEditor())){
					String htmlContent = MarkdownKit.pegDown(blog.getContent());
					blog.setSummary(HtmlFilterKit.truncate(htmlContent,300));
					searcherBean.setContent(htmlContent);
				}else{
					blog.setSummary(HtmlFilterKit.truncate(blog.getContent(),300));
					searcherBean.setContent(blog.getContent());
				}
				blog.save();
				
				//加入全文检索文档
				searcherBean.setId(String.valueOf(blog.getId()));
				searcherBean.setTitle(blog.getTitle());
				searcherBean.setSummary(blog.getSummary());
				SearcherKit.add(searcherBean);
			}
			
			//同步标签
			tagService.synBlogTag(blog.getTags());
			new Thread(new Runnable() {
				@Override
				public void run() {
					//重新统计分类下面的文章数量
					CategoryService.me.countCategoryHasBlog();
					//重新统计标签下面的文章数量
					tagService.countTagHasBlog();
				}
			}).start();
			CacheKit.removeAll(blogCacheName);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg",e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	/**
	 * 删除文章
	 * @param id 文章ID
	 * @return
	 */
	public Ret deleteById(Long id) {
		try {
			blogDao.deleteById(id);
			CacheKit.removeAll(blogCacheName);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
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
	public Ret change(Long id, String type) {
		try {
			StringBuffer sql = new StringBuffer("UPDATE tb_blog SET");
			switch (type) {
			case "privacy":
				sql.append(" privacy = IF(privacy = 0,1,0)");
				break;
			case "featured":
				sql.append(" featured = IF(featured = 0,1,0)");
				break;
			case "status":
				sql.append(" status = IF(status = 0,1,0)");
				break;
			default:
				break;
			}
			sql.append(" WHERE id = ?");
			CacheKit.removeAll(blogCacheName);
			Db.update(sql.toString(),id);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
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
			StringBuffer sql = new StringBuffer("SELECT id ,title,views FROM tb_blog where privacy = 0");
			switch (type) {
			case "views":
				sql.append(" ORDER BY views DESC LIMIT ?");
				break;
			case "news":
				sql.append(" ORDER BY createAt DESC LIMIT ?");
				break;
			case "featured":
				sql.append(" AND featured = 1 ORDER BY createAt DESC LIMIT ?");
				break;
			default:
				break;
			}
			List<Record> list = Db.findByCache(blogCacheName, String.format("FINDTOPN%dTYPE%s", n,type), sql.toString(), n);
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
	public Record findFullById(final Long blogId) {
		return CacheKit.get(blogCacheName, String.format("FINDFULLBYIDFOR%d", blogId), new IDataLoader() {
			String sql = "SELECT b.*,u.avatar authorAvatar,u.nickName authorName FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id WHERE b.id = ?";
			@Override
			public Record load() {
				Blog blog = blogDao.findFirstByCache(blogCacheName,String.format("FINDFULLBYIDFOR%d", blogId),sql,blogId);
				Record to = blog.toRecord();
				try {
					if(Constant.editorType.markdown.name().equals(blog.getEditor())){
						to.set("content", MarkdownKit.pegDown(blog.getContent()));
					}else{
						to.set("content", blog.getContent());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return to;
			}
		});
	}

	public List<Record> findList4Search() {
		StringBuffer sql = new StringBuffer("SELECT b.id,b.title,b.summary,b.content,b.editor");
		sql.append(" FROM tb_blog b WHERE b.privacy = 0 ORDER BY b.createAt DESC");
		return Db.findByCache(blogCacheName,"FINDALL4SEARCH",sql.toString());
	}
	
	public Record findById4Search(Integer id){
		StringBuffer sql = new StringBuffer("SELECT b.id,u.nickName authorName,b.createAt,b.views,b.tags,c.name categoryName");
		sql.append(" FROM tb_blog b LEFT JOIN tb_user u ON b.authorId = u.id LEFT JOIN tb_category c ON b.category = c.id");
		sql.append(" WHERE b.privacy = 0 AND b.id = ?");
		sql.append(" ORDER BY b.createAt DESC");
		return Db.findFirstByCache(blogCacheName, String.format("FINDBYID4SEARCHFOR%d", id),sql.toString(),id);
	}
	
	public void updateViewsCounts(Long blogId){
		Db.update("UPDATE tb_blog SET views = views + 1 WHERE id = ?", blogId);
		CacheKit.remove(blogCacheName, String.format("VIEWSCOUNTSFOR%d", blogId));
	}

	/**
	 * 
	 * 获取文章的浏览量
	 * @param blogId
	 * @return
	 */
	public Integer findViewsCount(Long blogId) {
		String cacheKey = String.format("VIEWSCOUNTSFOR%d", blogId);
		Object object = CacheKit.get(blogCacheName, cacheKey);
		int count = 0;
		
		if(object == null){
			count = Db.queryInt("SELECT views FROM tb_blog WHERE id = ?", blogId);
			CacheKit.put(blogCacheName, cacheKey, count);
		}else{
			count = (int)object;
		}
		return count;
	}
}
