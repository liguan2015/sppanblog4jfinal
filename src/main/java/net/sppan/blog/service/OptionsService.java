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
	public static enum optionKeyEnum{
		duoshuo_short_name,//多说插件短域名
		siteAboutMe,//关于我
		siteDescription,//站点描述
		siteDomain,//网站域名
		siteName,//网站名称
		defaultEditor;//默认编辑器
		@Override
		public String toString() {
			return super.toString();
		}
	}
	
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
			record.set("optionValue", content);
			record.set("optionKey", "siteAboutMe");
			Db.update("tb_options","optionKey",record);
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
	
	public Options findByOptionKey(String key){
		return optionsDao.findFirstByCache(optionsCacheName, String.format("FINDBYOPTIONKEYFOR%s", key), "SELECT * FROM tb_options WHERE optionKey = ?", key);
	}

}
