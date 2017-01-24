package net.sppan.jfinalblog.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class LoginValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		this.setShortCircuit(true);
		validateEmail("username", "msg", "请输入您的邮箱");
		validateString("password", 6,20, "msg", "密码长度不正确");
	}

	@Override
	protected void handleError(Controller c) {
		c.renderJson();
	}

}
