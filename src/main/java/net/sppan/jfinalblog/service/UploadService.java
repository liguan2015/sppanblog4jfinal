package net.sppan.jfinalblog.service;

import java.io.File;

import net.sppan.jfinalblog.model.User;
import net.sppan.jfinalblog.utils.ImageKit;

import org.joda.time.DateTime;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

/**
 * 上传业务
 * 1：不同模块分别保存到不同子目录
 * 2：每个目录下文件数达到 5000 时创建新的子目录，upload_counter 用于记录每个模块文件上传总数
 *    用于创建子目录
 */
public class UploadService {

	public static final UploadService me = new UploadService();

	/**
	 * 上传图片允许的最大尺寸，目前只允许 200KB
	 */
	public static final int imageMaxSize = 200 * 1024;

	/**
	 * 上传图片临时目录，相对于 baseUploadPath 的路径，是否以 "/" 并无影响
	 * 本项目的 baseUploadLoad 为 /var/www/project_name/upload
	 */
	public static final String uploadTempPath = "/img/temp";

	/**
	 * 相对于 webRootPath 之后的目录，与"/upload" 是与 baseUploadPath 重合的部分
	 */
	private static final String basePath = "/upload/img/";

	/**
	 * 每个子目录允许存 5000 个文件
 	 */
	public static final int FILES_PER_SUB_DIR = 5000;

	/**
	 * ueditor 上传业务方法
	 */
	public Ret ueditorUpload(User account, String uploadType, UploadFile uf) {
		Ret ret = checkUeditorUploadFile(uf);
		if (ret != null) {
			return ret;
		}

		String fileSize = uf.getFile().length() + "";
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
		 * ueditor 要求的返回格式：
		 * {"state": "SUCCESS",
		 * "title": "1465008328293017063.png",
		 * "original": "2222.png",
		 * "type": ".png",
		 * "url": "/ueditor/jsp/upload/image/20160604/1465008328293017063.png",
		 * "size": "185984" }
		 */
		return Ret.create("state", "SUCCESS")
				.set("url", relativePathFileName[0])
				.set("title", fileName[0])
				.set("original", uf.getOriginalFileName())
				.set("type", extName)
				.set("size", fileSize);
	}

	/**
	 * 生成规范的文件名
	 * accountId_年月日时分秒.jpg
	 * 包含 accountId 以便于找到某人上传的图片，便于定位该用户所有文章，方便清除恶意上传
	 * 图片中添加一些 meta 信息：accountId_201604161359.jpg
	 * 目录中已经包含了模块名了，这里的 meta 只需要体现 accountId 与时间就可以了
	 */
	private String generateFileName(Integer accountId, String extName) {
		DateTime dt = DateTime.now();
		return accountId + "_" + dt.toString("yyyyMMddHHmmss") + extName;
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
	 * 检查 ueditor 上传图片的合法性，返回值格式需要符合 ueditor 的要求
	 */
	private Ret checkUeditorUploadFile(UploadFile uf) {
		if (uf == null || uf.getFile() == null) {
			return Ret.create("state", "上传文件为 null");
		}
		if (ImageKit.notImageExtName(uf.getFileName())) {
			uf.getFile().delete();      // 非图片类型，立即删除，避免浪费磁盘空间
			return Ret.create("state", "只支持 jpg、jpeg、png、bmp 四种图片类型");
		}
		if (uf.getFile().length() > imageMaxSize) {
			uf.getFile().delete();      // 图片大小超出范围，立即删除，避免浪费磁盘空间
			return Ret.create("state", "图片尺寸只允许 200K 大小");
		}
		return null;
	}
}
