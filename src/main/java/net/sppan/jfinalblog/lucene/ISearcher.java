package net.sppan.jfinalblog.lucene;

import com.jfinal.plugin.activerecord.Page;

public interface ISearcher {

	public void init();

	public void addBean(SearcherBean bean);

	public void deleteBean(String beanId);

	public void updateBean(SearcherBean bean);

	public Page<SearcherBean> search(String keyword);

	public Page<SearcherBean> search(String queryString, int pageNum, int pageSize);
}
