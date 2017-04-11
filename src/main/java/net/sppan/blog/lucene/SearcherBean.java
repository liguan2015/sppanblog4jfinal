package net.sppan.blog.lucene;

import com.jfinal.kit.JMap;

public class SearcherBean {

	private String id;

	private String title;

	private String summary;

	private String content;

	/**
	 * 额外数据 用于存储createdAt views authorName
	 */
	private JMap data;

	public SearcherBean() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public JMap getData() {
		return data;
	}

	public void setData(JMap data) {
		this.data = data;
	}

	public SearcherBean(String id, String title, String summary,
			String content, JMap data) {
		super();
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.content = content;
		this.data = data;
	}

}
