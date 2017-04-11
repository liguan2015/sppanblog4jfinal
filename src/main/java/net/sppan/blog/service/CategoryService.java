package net.sppan.blog.service;

import java.util.List;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import net.sppan.blog.model.Category;

public class CategoryService {

	public static final CategoryService me = new CategoryService();
	private final Category categoryDao = new Category().dao();
	public static final String categoryCacheName = "categoryCache";
	
	/**
	 * 获取分页
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Category> getPage(Integer pageNumber, Integer pageSize) {
		return categoryDao.paginateByCache(categoryCacheName, String.format("GETPAGEFOR%dTO%d", pageNumber,pageSize), pageNumber, pageSize, "SELECT *", "FROM tb_category");
	}

	/**
	 * 通过ID查询
	 * @param id
	 * @return
	 */
	public Category findById(final Integer id) {
		return CacheKit.get(categoryCacheName, String.format("FINDBYIDFOR%d", id), new IDataLoader() {
			@Override
			public Category load() {
				return categoryDao.findById(id);
			}
		});
	}

	/**
	 * 保存或者更新
	 * @param category
	 * @return
	 */
	public Ret saveOrUpdate(Category category) {
		try {
			if(category.getId() != null){
				category.update();
			}else{
				category.setCount(0);
				category.save();
			}
			CacheKit.removeAll(categoryCacheName);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg",e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	/**
	 * 根据ID删除
	 * @param id
	 * @return
	 */
	public Ret deleteById(Integer id) {
		try {
			categoryDao.deleteById(id);
			CacheKit.removeAll(categoryCacheName);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	/**
	 * 查询所有标签
	 * @return
	 */
	public List<Category> findAll() {
		return categoryDao.findByCache(categoryCacheName, "FINDALL", "SELECT * FROM tb_category");
	}
	
	/**
	 * 查询所有可见标签
	 * 
	 * @return
	 */
	public List<Category> findVisible() {
		return categoryDao.findByCache(categoryCacheName, "FINDALL", "SELECT * FROM tb_category WHERE status = 0");
	}
	
	/**
	 * 改变标签的可用状态
	 * @param id
	 * @return
	 */
	public Ret changeStatus(Integer id) {
		try {
			String sql = "UPDATE tb_category SET status = IF(status = 0,1,0) WHERE id =?";
			Db.update(sql,id);
			CacheKit.removeAll(categoryCacheName);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg","操作成功");
	}
	
}
