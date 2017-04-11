package net.sppan.blog.directive;

import java.io.Writer;
import java.util.List;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.stat.Scope;

import net.sppan.blog.model.Tag;
import net.sppan.blog.service.TagService;

public class TagDirective extends Directive{
	
	private final TagService service = TagService.me;

	@Override
	public void exec(Env env, Scope scope, Writer writer) {
		List<Tag> list = service.findAll();
		scope.setLocal("list", list);
		stat.exec(env, scope, writer);
	}

	@Override
	public void setExprList(ExprList exprList) {
		super.setExprList(exprList);
	}

	@Override
	public boolean hasEnd() {
		return true;
	}
	
	

}
