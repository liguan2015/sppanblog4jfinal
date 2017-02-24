/**
 * 请勿将俱乐部专享资源复制给其他人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目文档与设计资源、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多
 * 
 * JFinal 俱乐部是五年以来首次寻求外部资源的尝试，以便于有资源创建更加
 * 高品质的产品与服务，为大家带来更大的价值，所以请大家多多支持，不要将
 * 首次的尝试扼杀在了摇篮之中
 */

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
			renderJson(Ret.create("success", 0).set("message", "没有登录不能上传文件"));
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
			renderJson(Ret.create("success", 0).set("message", "上传图片出现未知异常，请告知管理员：" + e.getMessage()));
			// 分析异常原因，看是否真的是文件大小超出范围，jfinal 后续版本考虑提供一个专用异常来解决此问题
			LogKit.error(e.getMessage(), e);
		}
	}
}
