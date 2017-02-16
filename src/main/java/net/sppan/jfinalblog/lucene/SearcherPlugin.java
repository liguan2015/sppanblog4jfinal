package net.sppan.jfinalblog.lucene;

import java.util.List;

import net.sppan.jfinalblog.utils.ClassUtils;

import com.jfinal.kit.Prop;
import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

public class SearcherPlugin implements IPlugin {

	static final Log log = Log.getLog(SearcherPlugin.class);

	private static ISearcher mSearcher;
	
	private Prop prop;
	
	private SearcherPlugin(){}
	
	public SearcherPlugin(Prop configProp) {
		prop = configProp;
	}

	public void initSearcher(Class<? extends ISearcher> clazz) {
		try {
			mSearcher = (ISearcher) clazz.newInstance();
			mSearcher.init(prop.get("lucenePath"));
			SearcherKit.init(mSearcher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public boolean start() {
		List<Class<ISearcher>> list = ClassUtils.scanSubClass(ISearcher.class, true);

		if (list == null || list.isEmpty()) {
			log.error("cant scan ISearcher implement class in class path.");
			return true;
		}

		if (list.size() > 1) {
			log.warn("there are too many searcher");
		}
	
		if (list.size() == 1) initSearcher(list.get(0));
		else {
			for (Class<ISearcher> iSearcherClass : list) {
				if (iSearcherClass.isAnnotationPresent(Current.class)) {
					initSearcher(iSearcherClass);
					break;
				}
			}
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
