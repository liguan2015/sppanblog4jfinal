
$(document).ready(function() {
	setCurrentNavMenu();
});

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
