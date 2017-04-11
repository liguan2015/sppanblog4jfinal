package net.sppan.blog.directive;

import java.io.Writer;
import java.util.List;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.stat.Scope;

import net.sppan.blog.model.Category;
import net.sppan.blog.service.CategoryService;

public class CategoryDirective extends Directive{
	
	private final CategoryService service = CategoryService.me;

	@Override
	public void exec(Env env, Scope scope, Writer writer) {
		List<Category> list = service.findVisible();
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
