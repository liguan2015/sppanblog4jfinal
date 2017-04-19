package net.sppan.blog.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

import net.sppan.blog.model.Options;
import net.sppan.blog.service.OptionsService;

public class OptionsHandler extends Handler{

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		//获取系统参数配置
		for (Options options : OptionsService.optionsList) {
			request.setAttribute(options.getOptionKey(), options.getOptionValue());
		}
		next.handle(target, request, response, isHandled);
	}

}
