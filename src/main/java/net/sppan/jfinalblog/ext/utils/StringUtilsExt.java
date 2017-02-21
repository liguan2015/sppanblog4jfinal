package net.sppan.jfinalblog.ext.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author SPPan
 *
 * String工具扩展，共享到模版中使用
 */
public class StringUtilsExt {
	
	private static final Logger logger = LoggerFactory.getLogger(StringUtilsExt.class);

	/**
	 * 字符串分割
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static String[] splitExt(String str,String regex){
		String[] split;
		try {
			split = str.split(regex);
			return split;
		} catch (Exception e) {
			logger.warn("splitExt方法中传入的参数【{}】【{}】不正确",str,regex);
		}
		return null;
	}
}
