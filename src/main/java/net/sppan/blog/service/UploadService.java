package net.sppan.blog.service;

import java.io.File;

import org.joda.time.DateTime;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

import net.sppan.blog.model.User;
import net.sppan.blog.service.OptionsService.optionKeyEnum;
import net.sppan.blog.utils.ImageKit;

/**
 * 上传业务
 */
public class UploadService {

	public static final UploadService me = new UploadService();

	/**
	 * 上传图片允许的最大尺寸，目前只允许 200KB
	 */
	public static final int imageMaxSize = 200 * 1024;

	/**
	 * 相对于 webRootPath 之后的目录，与"/upload" 是与 baseUploadPath 重合的部分
	 */
	private static final String basePath = "/upload/img/";

	/**
	 * 上传业务方法
	 */
	public Ret upload(User account, String uploadType, UploadFile uf) {
		Ret ret = checkUploadFile(uf);
		if (ret != null) {
			return ret;
		}

		String extName = "." + ImageKit.getExtName(uf.getFileName());

		// 相对路径 + 文件名：用于返回 ueditor 要求的 url 字段值，形如："/upload/img/project/0/123.jpg
		String[] relativePathFileName = new String[1];
		// 绝对路径 + 文件名：用于保存到文件系统
		String[] absolutePathFileName = new String[1];
		// 生成的文件名
		String[] fileName = new String[1];
		buildPathAndFileName(uploadType, account.getId(), extName, relativePathFileName, absolutePathFileName, fileName);
		saveOriginalFileToTargetFile(uf.getFile(), absolutePathFileName[0]);

		/**
		 * 要求的返回格式：
		 * {"success": 0|1,
		 * "url": "/upload/image/20160604/1465008328293017063.png",
		 * "message": "185984" }
		 */
		return Ret.create("success", true).set("file_path", OptionsService.me.findByOptionKey(optionKeyEnum.siteDomain.name()).getOptionValue() + relativePathFileName[0]);
	}

	/**
	 * 生成规范的文件名
	 * userId年月日时分秒.jpg
	 * 包含 userId 以便于找到某人上传的图片，便于定位该用户所有文章，方便清除恶意上传
	 * 图片中添加一些 meta 信息：userId_201604161359.jpg
	 * 目录中已经包含了模块名了，这里的 meta 只需要体现 userId 与时间就可以了
	 */
	private String generateFileName(Integer userId, String extName) {
		DateTime dt = DateTime.now();
		return userId + "_" + dt.toString("yyyyMMddHHmmss") + extName;
	}

	/**
	 * 根据上传类型生成完整的文件保存路径
	 * @param uploadType 上传类型，目前支持四种：project, share, feedback, document
	 */
	private void buildPathAndFileName(
			String uploadType,
			Integer accountId,
			String extName,
			String[] relativePathFileName,
			String[] absolutePathFileName,
			String[] fileName) {

		String relativePath = "/";    // 生成相对对路径
		relativePath = basePath + uploadType + relativePath;

		fileName[0] = generateFileName(accountId, extName);
		relativePathFileName[0] =  relativePath + fileName[0];

		String absolutePath = PathKit.getWebRootPath() + relativePath;   // webRootPath 将来要根据 baseUploadPath 调整，改代码，暂时选先这样用着，着急上线
		File temp = new File(absolutePath);
		if (!temp.exists()) {
			temp.mkdirs();  // 如果目录不存在则创建
		}
		absolutePathFileName[0] = absolutePath + fileName[0];
	}

	/**
	 * 目前使用 File.renameTo(targetFileName) 的方式保存到目标文件，
	 * 如果 linux 下不支持，或者将来在 linux 下要跨磁盘保存，则需要
	 * 改成 copy 文件内容的方式并删除原来文件的方式来保存
	 */
	private void saveOriginalFileToTargetFile(File originalFile, String targetFile) {
		originalFile.renameTo(new File(targetFile));
	}

	/**
	 * 检查传图片的合法性
	 */
	private Ret checkUploadFile(UploadFile uf) {
		if (uf == null || uf.getFile() == null) {
			return Ret.create("success",false).set("msg", "上传文件为 null");
		}
		if (ImageKit.notImageExtName(uf.getFileName())) {
			uf.getFile().delete();      // 非图片类型，立即删除，避免浪费磁盘空间
			return Ret.create("success",false).set("msg", "只支持 jpg、jpeg、png、bmp 四种图片类型");
		}
		if (uf.getFile().length() > imageMaxSize) {
			uf.getFile().delete();      // 图片大小超出范围，立即删除，避免浪费磁盘空间
			return Ret.create("success",false).set("msg", "图片尺寸只允许 200K 大小");
		}
		return null;
	}
}
