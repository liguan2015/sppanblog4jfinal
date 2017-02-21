package net.sppan.jfinalblog.directive;

import java.io.Writer;
import java.util.List;

import net.sppan.jfinalblog.model.Category;
import net.sppan.jfinalblog.service.CategoryService;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.stat.Scope;

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
