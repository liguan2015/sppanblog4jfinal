package net.sppan.jfinalblog.service;

import java.util.List;

import net.sppan.jfinalblog.model.Tag;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

public class TagService {

	public static final TagService me = new TagService();
	public static final String tagCache = "tagCache";
	private final Tag tagDao = new Tag().dao();
	
	public Page<Tag> getPage(Integer pageNumber, Integer pageSize) {
		return tagDao.paginateByCache(tagCache,String.format("GETPAGEFOR%dTO%d", pageNumber,pageSize),pageNumber, pageSize, "SELECT *", "FROM tb_tag");
	}

	public Tag findById(final Integer id) {
		return CacheKit.get(tagCache, String.format("FINDBYIDFOR%d", id), new IDataLoader() {
			@Override
			public Tag load() {
				return tagDao.findById(id);
			}
		});
	}

	public Ret saveOrUpdate(Tag tag) {
		try {
			if(tag.getId() != null){
				tag.update();
			}else{
				tag.save();
			}
			CacheKit.removeAll(tagCache);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg",e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret deleteById(Integer id) {
		try {
			tagDao.deleteById(id);
			CacheKit.removeAll(tagCache);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret changeStatus(Integer id) {
		try {
			String sql = "UPDATE tb_tag SET status = IF(status = 0,1,0) WHERE id =?";
			Db.update(sql,id);
			CacheKit.removeAll(tagCache);
		} catch (Exception e) {
			e.printStackTrace();
			return Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg","操作成功");
	}

	public List<Tag> findAll() {
		return tagDao.findByCache(tagCache,"FINDALL","select * from tb_tag WHERE status = 0 ORDER BY count DESC");
	}

	public List<String> findAllNameList() {
		return CacheKit.get(tagCache, "FINDALLNAMELIST", new IDataLoader() {
			@Override
			public List<String> load() {
				return Db.query("SELECT name FROM tb_tag");
			}
		});
	}
	
	public Tag findByName(final String tagName){
		return tagDao.findFirstByCache(tagCache, String.format("FINDBYNAMEFOR%s", tagName), "SELECT * FROM tb_tag WHERE name = ?", tagName);
	}

	/**
	 * 把所有标签同步到标签表中,标签文章数量只加不减
	 * @param tags
	 */
	public void synBlogTag(String tags) {
		if(StrKit.notBlank(tags)){
			String[] split = tags.split(",");
			
			for (String tagName : split) {
				Tag dbTag = findByName(tagName);
				if(dbTag == null){
					dbTag = new Tag();
					dbTag.setName(tagName);
					dbTag.setCount(1);
				}else{
					//标签统计+1
					Integer oldCount = dbTag.getCount();
					if(oldCount == null){
						oldCount = 0;
					}
					dbTag.setCount(oldCount + 1);
				}
				saveOrUpdate(dbTag);
			}
		}
	}
	
}
