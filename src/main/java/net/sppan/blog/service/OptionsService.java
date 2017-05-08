package net.sppan.blog.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sppan.blog.model.Options;

/**
 * @author SPPan
 *
 */
public class OptionsService {
	private final Options optionsDao = new Options().dao();
	public static final OptionsService me  = new OptionsService();
	
	/**
	 * 
	 * @author SPPan
	 * 
	 * 系统配置项
	 *
	 */
	public static enum optionKeyEnum{
		duoshuo_short_name,//多说插件短域名
		siteAboutMe,//关于我
		siteDescription,//站点描述
		siteDomain,//网站域名
		siteName,//网站名称
		defaultEditor,//默认编辑器
		siteIcp; //网站ICP
		@Override
		public String toString() {
			return super.toString();
		}
	}
	
	/**
	 * 用于存储系统变量
	 */
	public static List<Options> optionsList = new ArrayList<Options>();
	
	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<Options> findAll(){
		return optionsDao.find("SELECT * FROM tb_options");
	}
	
	public Ret updateAboutMe(String content){
		try {
			Record record = new Record();
			record.set("optionValue", content);
			record.set("optionKey", optionKeyEnum.siteAboutMe.name());
			Db.update("tb_options","optionKey",record);
			
			//更新系统变量
			init();
			
			return Ret.ok("msg","操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
	}
	
	/**
	 * 根据简直查询
	 * @param key
	 * @return
	 */
	public Options findByOptionKey(String key){
		return optionsDao.findFirst("SELECT * FROM tb_options WHERE optionKey = ?", key);
	}

	/**
	 * 查询分页数据
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Options> getPage(Integer pageNumber, Integer pageSize) {
		Page<Options> page = optionsDao.paginate(pageNumber, pageSize, "SELECT *", "FROM tb_options WHERE optionKey <> ?", optionKeyEnum.siteAboutMe.name());
		return page;
	}
	
	/**
	 * 初始化系统配置
	 */
	public void init(){
		optionsList = findAll();
	}

	/**
	 * 更新系统配置项
	 * @param options
	 * @return
	 */
	public Ret updateByOptionKey(Options options) {
		if(StrKit.isBlank(options.getOptionKey()) || StrKit.isBlank(options.getOptionValue())){
			return Ret.fail("msg", "配置项有误");
		}
		try {
			options.update();
			//更新系统变量
			init();
			return Ret.ok("msg","操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
	}

}
