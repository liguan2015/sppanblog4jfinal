package net.sppan.jfinalblog.lucene;

import java.util.Date;

public class SearcherBean {

	private String id;

	private String title;

	private String summary;

	private String content;

	private Date createAt;

	private String authorName;

	private Integer views;

	public SearcherBean() {
	}

	public SearcherBean(String id, String title, String summary,
			String content, Date createAt, String authorName, Integer views) {
		super();
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.content = content;
		this.createAt = createAt;
		this.authorName = authorName;
		this.views = views;
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

}
