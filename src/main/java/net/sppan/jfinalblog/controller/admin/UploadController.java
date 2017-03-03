package net.sppan.jfinalblog.controller.admin;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.service.UploadService;

import com.jfinal.kit.LogKit;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

public class UploadController extends BaseController {

	static UploadService srv = UploadService.me;

	public void image() {

		if (notLogin()) {
			renderJson(Ret.create("success", false).set("msg", "没有登录不能上传文件"));
			return ;
		}

		UploadFile uploadFile = null;
		try {
			uploadFile = getFile();
			Ret ret = srv.upload(getLoginUser(), "uploadimage", uploadFile);
			renderJson(ret);
		} catch(Exception e) {
			if (uploadFile != null) {
				uploadFile.getFile().delete();
			}
			renderJson(Ret.create("success", false).set("msg", "上传图片出现未知异常，请告知管理员：" + e.getMessage()));
			// 分析异常原因，看是否真的是文件大小超出范围，jfinal 后续版本考虑提供一个专用异常来解决此问题
			LogKit.error(e.getMessage(), e);
		}
	}
}
