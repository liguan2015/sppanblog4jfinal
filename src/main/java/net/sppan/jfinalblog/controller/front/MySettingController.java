package net.sppan.jfinalblog.controller.front;

import net.sppan.jfinalblog.controller.BaseController;
import net.sppan.jfinalblog.model.User;
import net.sppan.jfinalblog.service.LoginService;
import net.sppan.jfinalblog.service.MySettingService;

import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

public class MySettingController extends BaseController {
	private final MySettingService srv = MySettingService.me;
	
	public void info(){
		render("info.html");
	}
	
	public void password(){
		render("password.html");
	}
	
	/**
	 * 上传用户图片，为裁切头像做准备
	 */
	public void uploadAvatar() {
		UploadFile uf = null;
		try {
			uf = getFile("avatar", srv.getAvatarTempDir(), srv.getAvatarMaxSize());
			if (uf == null) {
				renderJson(Ret.fail("msg", "请先选择上传文件"));
				return;
			}
		} catch (Exception e) {
			// 经测试，暂时拿不到这个异常，需要改进 jfinal 才可以拿得到
			if (e instanceof com.oreilly.servlet.multipart.ExceededSizeException) {
				renderJson(Ret.fail("msg", "文件大小超出范围"));
			} else {
				if (uf != null) {
					// 只有出现异常时才能删除，不能在 finally 中删，因为后面需要用到上传文件
					uf.getFile().delete();
				}
				renderJson(Ret.fail("msg", e.getMessage()));
			}
			return ;
		}

		Ret ret = srv.uploadAvatar(123456, uf);
		if (ret.isOk()) {   // 上传成功则将文件 url 径暂存起来，供下个环节进行裁切
			setSessionAttr("avatarUrl", ret.get("avatarUrl"));
		}
		renderJson(ret);
	}

	/**
	 * 保存 jcrop 裁切区域为用户头像
	 */
	public void saveAvatar() {
		String avatarUrl = getSessionAttr("avatarUrl");
		int x = getParaToInt("x");
		int y = getParaToInt("y");
		int width = getParaToInt("width");
		int height = getParaToInt("height");
		Ret ret = srv.saveAvatar(getLoginUser(), avatarUrl, x, y, width, height);
		renderJson(ret);
	}

	public void updatePassword() {
		User loginAccount = getAttr(LoginService.loginUserCacheName);
		Ret ret = srv.updatePassword(loginAccount.getId(), getPara("oldPassword"), getPara("newPassword"));
		renderJson(ret);
	}

}
