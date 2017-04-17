package net.sppan.blog.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("tb_blog", "id", Blog.class);
		arp.addMapping("tb_category", "id", Category.class);
		arp.addMapping("tb_options", "key", Options.class);
		arp.addMapping("tb_session", "id", Session.class);
		arp.addMapping("tb_tag", "id", Tag.class);
		arp.addMapping("tb_user", "id", User.class);
		arp.addMapping("tb_youlian", "id", Youlian.class);
	}
}

