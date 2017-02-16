package net.sppan.jfinalblog.lucene;

import com.jfinal.kit.Prop;
import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

public class SearcherPlugin implements IPlugin {

	static final Log log = Log.getLog(SearcherPlugin.class);

	private static ISearcher mSearcher;
	
	private Prop prop;
	
	public SearcherPlugin(){
		
	}
	
	public SearcherPlugin(Prop configProp,ISearcher iSearcher) {
		prop = configProp;
		mSearcher = iSearcher;
	}

	@Override
	public boolean start() {
		try {
			mSearcher.init(prop);
			SearcherKit.init(mSearcher);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
