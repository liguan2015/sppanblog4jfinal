package net.sppan.blog.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

import net.sppan.blog.model.Options;

public class OptionsService {
	private final Options optionsDao = new Options().dao();
	public static final String optionsCacheName = "optionsCacheName";
	public static final OptionsService me  = new OptionsService();
	
	/**
	 * 用于存储系统变量
	 */
	public static List<Options> optionsList = new ArrayList<Options>();
	
	public List<Options> findAll(){
		return optionsDao.findByCache(optionsCacheName, "FINDALL", "SELECT * FROM tb_options");
	}
	
	public Ret updateAboutMe(String content){
		try {
			Record record = new Record();
			record.set("value", content);
			record.set("key", "siteAboutMe");
			Db.update("tb_options","key",record);
			//清除缓存
			CacheKit.removeAll(optionsCacheName);
			//更新系统变量
			optionsList = findAll();
			return Ret.ok("msg","操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
	}

}
