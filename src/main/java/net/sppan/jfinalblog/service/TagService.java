package net.sppan.jfinalblog.service;

import java.util.List;

import net.sppan.jfinalblog.model.Tag;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class TagService {

	public static final TagService me = new TagService();
	private final Tag tagDao = new Tag().dao();
	
	public Page<Tag> getPage(Integer pageNumber, Integer pageSize) {
		return tagDao.paginate(pageNumber, pageSize, "SELECT *", "FROM tb_tag");
	}

	public Tag findById(Integer id) {
		return tagDao.findById(id);
	}

	public Ret saveOrUpdate(Tag tag) {
		try {
			if(tag.getId() != null){
				tag.update();
			}else{
				tag.setCount(0);
				tag.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg",e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret deleteById(Integer id) {
		try {
			tagDao.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret changeStatus(Integer id) {
		try {
			String sql = "UPDATE tb_tag SET status = IF(status = 0,1,0) WHERE id =?";
			Db.update(sql,id);
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg","操作成功");
	}

	public List<Tag> findAll() {
		return tagDao.find("select * from tb_tag WHERE status = 0 ORDER BY count DESC");
	}
	
}
