package net.sppan.blog.service;

import java.util.List;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import net.sppan.blog.model.Youlian;

public class YoulianService {

	public static final YoulianService me = new YoulianService();
	private final Youlian youlianDao = new Youlian().dao();
	public static final String youlianCache = "youlianCacheName";
	
	public List<Youlian> findAll() {
		return youlianDao.findByCache(youlianCache, "FINDALL", "SELECT * FROM tb_youlian");
	}
	
	public List<Youlian> findVisiable() {
		return youlianDao.findByCache(youlianCache, "FINDVISIABLE", "SELECT * FROM tb_youlian WHERE status = 0");
	}

	public Page<Youlian> getPage(Integer pageNumber, Integer pageSize) {
		return youlianDao.paginateByCache(youlianCache,String.format("GETPAGEFOR%dTO%d", pageNumber,pageSize),pageNumber, pageSize, "SELECT *", "FROM tb_youlian");
	}

	public Youlian findById(final Integer id) {
		return CacheKit.get(youlianCache, String.format("FINDBYIDFOR%d", id), new IDataLoader() {
			@Override
			public Youlian load() {
				return youlianDao.findById(id);
			}
		});
	}

	public Ret saveOrUpdate(Youlian youlian) {
		try {
			youlian.setStatus(0);
			if(youlian.getId() != null){
				youlian.update();
			}else{
				youlian.save();
			}
			CacheKit.removeAll(youlianCache);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg",e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret deleteById(Integer id) {
		try {
			youlianDao.deleteById(id);
			CacheKit.removeAll(youlianCache);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret changeStatus(Integer id) {
		try {
			String sql = "UPDATE tb_youlian SET status = IF(status = 0,1,0) WHERE id =?";
			Db.update(sql,id);
			CacheKit.removeAll(youlianCache);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg","操作成功");
	}
}
