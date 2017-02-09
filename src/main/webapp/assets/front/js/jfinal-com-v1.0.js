
$(document).ready(function() {
	setCurrentNavMenu();
});

/**
 *  采用问号挂参的方式，为 a 链接追加 returnUrl 参数
 */
function appendReturnUrl(target) {
	var returnUrl;
	var currentUrl = location.pathname;
	if (currentUrl.indexOf("/login") != 0 && currentUrl.indexOf("/reg") != 0) {
		returnUrl = "?returnUrl=" + currentUrl;
		var link = $(target);
		link.attr("href", link.attr("href") + returnUrl);
	}
}

/**
 * 退出登录
 */
function logout() {
	if (confirm('确定要退出登录？')) {
		location.href = '/logout';
	} 
}

/**
 * 设置当前导航菜单
 */
function setCurrentNavMenu() {
	var url = location.pathname, navMenus = $(".jf-nav-menu-box a");
	$.each(navMenus, function(p1, p2) {
		if (!url.indexOf('/c/' + p1)){
			navMenus.eq(p1-1).addClass("jf-nav-menu-current");
		}
	});
}

/**
 * 设置当前我的空间菜单
 */
function setCurrentMyMenu() {
	var url = location.pathname, navMenus = $(".jf-my-menu li");
	if (url == '/my') {
		navMenus.eq(0).addClass("jf-my-menu-current");
	} else if (!url.indexOf('/my/setting/info')) {
		navMenus.eq(1).addClass("jf-my-menu-current");
	} else if (!url.indexOf('/my/setting/password')) {
		navMenus.eq(2).addClass("jf-my-menu-current");
	}
}

/**
 *  textarea 不要设置 margin 值，否则 IE 下的 scrollHeight 会包含该值，用外部嵌套div来布局
 * @param ele 必须是 textarea，并且在外部需要将 overflow 设置为 hidden
 * @param minHeight 最小高度值
 */
function autoHeight(ele, minHeight) {
	minHeight = minHeight || 16;
	if (ele.style.height) {
		ele.style.height = (parseInt(ele.style.height) - minHeight ) + "px";
	}
	ele.style.height = ele.scrollHeight + "px";
}

// 来自 git.oschina.net 项目首页，只支持自动增高，不支持减高
// textarea 自动调整高度，绑定 onkeyup="textAreaAdjustHeight(this);"
// git.oschina.net 的 issue 回复实现减高功能，但找不到代码
function textareaAdjustHeightOsc(textarea) {
	var adjustedHeight = textarea.clientHeight;
	adjustedHeight = Math.max(textarea.scrollHeight, adjustedHeight);
	if (adjustedHeight > textarea.clientHeight) {
		textarea.style.height = adjustedHeight + 'px';
	}
}


/**
 * ajax GET 请求封装，提供了一些默认参数
 */
function ajaxGet(url, options) {
	var defaultOptions = {
		type: "GET"
		, cache: false      // 经测试设置为 false 时，ajax 请求会自动追加一个参数 "&_=nnnnnnnnnnn"
		, dataType: "json"  // "json" "text" "html" "jsonp"，如果设置为"html"，其中的script会被执行
		// , data: {}
		// , timeout: 9000     // 毫秒
		// , beforeSend: function(XHR) {}
		, success: function(ret){
			if (ret.isOk) {
				alert(ret.msg ? ret.msg : "操作成功");
			} else {
				alert("操作失败：" + (ret.msg ? ret.msg : "请告知管理员！"));
			}
		}
		, error: function(XHR, msg) {
			showReplyErrorMsg(msg); // 默认调用
		}
		// , complete: function(XHR, msg){} // 请求成功与失败都调用
	};
	// 用户自定义参数覆盖掉默认参数
	for(var o in options) {
		defaultOptions[o] = options[o];
	}

	$.ajax(url, defaultOptions);
}

/**
 * 确认对话框层，点击确定才真正操作
 * @param msg 对话框的提示文字
 * @param actionUrl 点击确认后请求到的目标 url
 * @param options jquery $.ajax(...) 方法的 options 参数
 */
function confirmAjaxGet(msg, actionUrl, options) {
	layer.confirm(msg, {
		icon: 0
		, title:''                                      // 设置为空串时，title消失，并自动切换关闭按钮样式，比较好的体验
		, shade: 0.4
		, offset: "139px"
	}, function(index) {                                // 只有点确定后才会回调该方法
		// location.href = operationUrl;                // 操作是一个 GET 链接请求，并非 ajax
		// 替换上面的 location.href 操作，改造成 ajax 请求。后端用 renderJson 更方便，不需要知道 redirect 到哪里
		ajaxGet(actionUrl, options);
		layer.close(index);                             // 需要调用 layer.close(index) 才能关闭对话框
	});
}
