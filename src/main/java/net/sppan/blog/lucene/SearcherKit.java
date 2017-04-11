package net.sppan.blog.lucene;

import com.jfinal.plugin.activerecord.Page;

public class SearcherKit {

	private static ISearcher mSearcher;

	static void init(ISearcher mSearcher) {
		SearcherKit.mSearcher = mSearcher;
	}

	public static void add(SearcherBean bean) {
		checkSearcher();
		mSearcher.addBean(bean);
	}

	public static void delete(String beanId) {
		checkSearcher();
		mSearcher.deleteBean(beanId);
	}

	public static void update(SearcherBean bean) {
		checkSearcher();
		mSearcher.updateBean(bean);
	}

	/**
	 * 执行搜索
	 * 
	 * @param keyword
	 * @return List<SearcherBean>
	 */
	public static Page<SearcherBean> search(String keyword) {
		checkSearcher();
		return mSearcher.search(keyword);
	}

	/**
	 * 分页搜索
	 * @param pageNum 当前页
	 * @param pageSize 每页条数
	 * @param keyword 关键字
	 * @return
	 */
	public static Page<SearcherBean> search(int pageNum, int pageSize, String keyword) {
		checkSearcher();
		return mSearcher.search(pageNum, pageSize, keyword);
	}

	/**
	 * 初始化检查
	 */
	public static void checkSearcher() {
		if (mSearcher == null) {
			throw new RuntimeException("must init searcher before,please invoke SearchFactory.use() to init.");
		}
	}
	
	/**
	 * 重检索引
	 */
	public static void reloadIndex(){
		checkSearcher();
		mSearcher.reloadIndex();
	}

}
