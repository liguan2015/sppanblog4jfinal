package net.sppan.jfinalblog.service;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import net.sppan.jfinalblog.model.Category;

public class CategoryService {

	public static final CategoryService me = new CategoryService();
	private final Category categoryDao = new Category().dao();
	
	public Page<Category> getPage(Integer pageNumber, Integer pageSize) {
		return categoryDao.paginate(pageNumber, pageSize, "select *", "from tb_category");
	}

	public Category findById(Integer id) {
		return categoryDao.findById(id);
	}

	public Ret saveOrUpdate(Category category) {
		try {
			if(category.getId() != null){
				category.update();
			}else{
				category.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg",e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}

	public Ret deleteById(Integer id) {
		try {
			categoryDao.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			Ret.fail("msg", e.getMessage());
		}
		return Ret.ok("msg", "操作成功");
	}
	
}
