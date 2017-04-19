/**
 * 剪贴板直接粘贴上传
 */
SimpleMDE.prototype.pasteUpload = function(editor, e) {
	if (!(e.clipboardData && e.clipboardData.items)) {
		alert("该浏览器不支持操作");
		return;
	}
	for (var i = 0, len = e.clipboardData.items.length; i < len; i++) {
		var item = e.clipboardData.items[i];
		// console.log(item.kind+":"+item.type);
		if (item.kind === "string") {
			item.getAsString(function(str) {
				// str 是获取到的字符串
			})
		} else if (item.kind === "file") {
			var resultJson;
			var data = new FormData();
			// pasteFile就是获取到的文件
			var pasteFile = item.getAsFile();
			var file = new File([pasteFile], "image.png", {type:"image/png"});
			data.append("upload_file", file);
			var xhr = new XMLHttpRequest();
			xhr.open("post", rootPath + "/upload/image", false);
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4) {
					resultJson = xhr.responseText;
				}
			};
			xhr.send(data);
			var json = eval('(' + resultJson + ')');
			if(json.success){
				var result = "![](" + json.file_path + ")";
				editor.replaceSelection(result);
			}else{
				alert(json.msg);
			}
		}
	}
}
/**
 * 拖拽图片上传
 */
SimpleMDE.prototype.dropUpload = function(editor, e) {
	if (!(e.dataTransfer && e.dataTransfer.files)) {
		alert("该浏览器不支持操作");
		return;
	}
	for (var i = 0; i < e.dataTransfer.files.length; i++) {
		console.log(e.dataTransfer.files[i]);
		var resultJson;
		var data = new FormData();
		data.append("upload_file", e.dataTransfer.files[i]);
		var xhr = new XMLHttpRequest();
		xhr.open("post", rootPath + "/upload/image", false);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				resultJson = xhr.responseText;
			}
		};
		xhr.send(data);
		var json = eval('(' + resultJson + ')');
		if(json.success){
			var result = "![](" + json.file_path + ")";
			editor.replaceSelection(result);
		}else{
			alert(json.msg);
		}
	}
	e.preventDefault();
}