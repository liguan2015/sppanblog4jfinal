package net.sppan.blog.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseBlog<M extends BaseBlog<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setAuthorId(java.lang.Long authorId) {
		set("authorId", authorId);
	}

	public java.lang.Long getAuthorId() {
		return get("authorId");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}

	public void setCreateAt(java.util.Date createAt) {
		set("createAt", createAt);
	}

	public java.util.Date getCreateAt() {
		return get("createAt");
	}

	public void setFeatured(java.lang.Integer featured) {
		set("featured", featured);
	}

	public java.lang.Integer getFeatured() {
		return get("featured");
	}

	public void setCategory(java.lang.Integer category) {
		set("category", category);
	}

	public java.lang.Integer getCategory() {
		return get("category");
	}

	public void setPrivacy(java.lang.Integer privacy) {
		set("privacy", privacy);
	}

	public java.lang.Integer getPrivacy() {
		return get("privacy");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setSummary(java.lang.String summary) {
		set("summary", summary);
	}

	public java.lang.String getSummary() {
		return get("summary");
	}

	public void setTags(java.lang.String tags) {
		set("tags", tags);
	}

	public java.lang.String getTags() {
		return get("tags");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return get("title");
	}

	public void setViews(java.lang.Integer views) {
		set("views", views);
	}

	public java.lang.Integer getViews() {
		return get("views");
	}

	public void setEditor(java.lang.String editor) {
		set("editor", editor);
	}

	public java.lang.String getEditor() {
		return get("editor");
	}

}
