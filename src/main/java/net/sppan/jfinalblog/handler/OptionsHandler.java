package net.sppan.jfinalblog.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sppan.jfinalblog.model.Options;
import net.sppan.jfinalblog.service.OptionsService;

import com.jfinal.handler.Handler;

public class OptionsHandler extends Handler{

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		//获取系统参数配置
		for (Options options : OptionsService.optionsList) {
			request.setAttribute(options.getKey(), options.getValue());
		}
		next.handle(target, request, response, isHandled);
	}

}
