package net.sppan.blog;

import java.io.File;
import java.sql.Connection;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;

import net.sppan.blog.directive.BlogDirective;
import net.sppan.blog.directive.CategoryDirective;
import net.sppan.blog.directive.TagDirective;
import net.sppan.blog.directive.ViewsCountDirective;
import net.sppan.blog.directive.YoulianDirective;
import net.sppan.blog.ext.utils.StringUtilsExt;
import net.sppan.blog.handler.OptionsHandler;
import net.sppan.blog.lucene.LuceneSearcher;
import net.sppan.blog.lucene.SearcherKit;
import net.sppan.blog.lucene.SearcherPlugin;
import net.sppan.blog.model._MappingKit;
import net.sppan.blog.routes.AdminRoutes;
import net.sppan.blog.routes.FrontRoutes;
import net.sppan.blog.service.OptionsService;
import net.sppan.blog.utils.DruidKit;

public class JFinalBlogConfig extends JFinalConfig {


	private static Prop p = loadConfig();
	private WallFilter wallFilter;
	/**
	 * 启动入口，运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 * 
	 * 使用本方法启动过第一次以后，会在开发工具的 debug、run configuration 中自动生成
	 * 一条启动配置项，可对该自动生成的配置再继续添加更多的配置项，例如 VM argument 可配置为：
	 * -XX:PermSize=64M -XX:MaxPermSize=256M
	 * 上述 VM 配置可以缓解热加载功能出现的异常
	 */
	public static void main(String[] args) {
		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
		JFinal.start("src/main/webapp", 80, "/", 5);
		
		/**
		 * 特别注意：IDEA 之下建议的启动方式，仅比 eclipse 之下少了最后一个参数
		 */
		// JFinal.start("src/main/webapp", 80, "/");
	}
	
	private static Prop loadConfig() {
		try {
			// 优先加载生产环境配置文件
			return PropKit.use("jfinalblog_config_pro.txt");
		} catch (Exception e) {
			// 找不到生产环境配置文件，再去找开发环境配置文件
			return PropKit.use("jfinalblog_config_dev.txt");
		}
	}
	
    public void configConstant(Constants me) {
        me.setDevMode(p.getBoolean("devMode", false));
		me.setJsonFactory(MixedJsonFactory.me());
    }
    
    /**
     * 路由拆分到 FrontRutes 与 AdminRoutes 之中配置的好处：
     * 1：可分别配置不同的 baseViewPath 与 Interceptor
     * 2：避免多人协同开发时，频繁修改此文件带来的版本冲突
     * 3：避免本文件中内容过多，拆分后可读性增强
     * 4：便于分模块管理路由
     */
    public void configRoute(Routes me) {
	    me.add(new FrontRoutes());
	    me.add(new AdminRoutes());
    }
    
    /**
     * 配置模板引擎，通常情况只需配置共享的模板函数
     */
    public void configEngine(Engine me) {
    	me.addDirective("tagDirective", new TagDirective());
    	me.addDirective("blogDirective", new BlogDirective());
    	me.addDirective("categoryDirective", new CategoryDirective());
    	me.addDirective("youlianDirective", new YoulianDirective());
    	
    	me.addDirective("viewsCountDirective", new ViewsCountDirective());
    	
    	me.addSharedMethod(new StringUtilsExt());
    	
//    	me.addSharedObject("ctx", JFinal.me().getContextPath());
    }
    
    /**
     * 抽取成独立的方法，例于 _Generator 中重用该方法，减少代码冗余
     */
	public static DruidPlugin getDruidPlugin() {
		return new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password").trim());
	}
	
    public void configPlugin(Plugins me) {
	    DruidPlugin druidPlugin = getDruidPlugin();
	    wallFilter = new WallFilter();              // 加强数据库安全
	    wallFilter.setDbType("mysql");
	    druidPlugin.addFilter(wallFilter);
	    druidPlugin.addFilter(new StatFilter());    // 添加 StatFilter 才会有统计数据
	    me.add(druidPlugin);
	    
	    ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
	    arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
	    
	    arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + File.separator + "sql"); //设置sql模版位置
	    arp.addSqlTemplate("all_sqls.sql");	//设置sql模版
	    
	    _MappingKit.mapping(arp);
	    me.add(arp);
        if (p.getBoolean("devMode", false)) {
            arp.setShowSql(true);
        }
        
		me.add(new EhCachePlugin());
		
		me.add(new Cron4jPlugin(p));
		
		me.add(new SearcherPlugin(p,new LuceneSearcher()));
    }
    
    public void configInterceptor(Interceptors me) {
    	
    }
    
    public void configHandler(Handlers me) {
	    me.add(DruidKit.getDruidStatViewHandler()); // druid 统计页面功能
	    me.add(new ContextPathHandler("ctx"));
	    me.add(new OptionsHandler());
    }
    
    /**
     * 本方法会在 jfinal 启动过程完成之后被回调，详见 jfinal 手册
     */
	public void afterJFinalStart() {
		// 让 druid 允许在 sql 中使用 union
		// https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter
		wallFilter.getConfig().setSelectUnionCheck(false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				OptionsService.me.init();
				//重检索引
				SearcherKit.reloadIndex();
			}
		}).start();
	}
}