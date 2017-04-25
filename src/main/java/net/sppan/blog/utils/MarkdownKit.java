package net.sppan.blog.utils;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class MarkdownKit {
	private final static PegDownProcessor md = new PegDownProcessor(Extensions.ALL_WITH_OPTIONALS);
	public static String pegDown(String content) {
	    return md.markdownToHtml(content);
	}
}
