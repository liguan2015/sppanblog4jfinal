package net.sppan.jfinalblog.model.support;


public enum Operators {
	/**
	 * 等于
	 */
	EQ,
	/**
	 * 大于
	 */
	GT,
	/**
	 *小于
	 */
	LT,
	/**
	 * 大于等于
	 */
	GE,
	/**
	 * 小于等于
	 */
	LE,
	/**
	 * 包含
	 */
	IN,
	/**
	 * 不包含
	 */
	NOT_IN,
	/**
	 * 模糊
	 */
	LIKE
	/**
	 * 不等于
	 */
	, NOT_EQ
	/**
	 * 范围查询
	 */
	, BETWEEN
}
