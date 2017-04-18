package net.sppan.blog.directive;

import java.io.Writer;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;

import net.sppan.blog.service.BlogService;

public class ViewsCountDirective extends Directive{
	private Expr expr;
	
	private final BlogService blogService = BlogService.me;

	@Override
	public void exec(Env env, Scope scope, Writer writer) {
		Object object = expr.eval(scope);
		Integer count = blogService.findViewsCount((Long)object);
		write(writer, count.toString());
	}

	@Override
	public void setExprList(ExprList exprList) {
		Expr[] exprs = exprList.getExprArray();
		if(exprs.length == 1){
			expr = exprs[0];
		}else{
			throw new ParseException("Wrong number parameter of #viewsCount directive, noly one parameter allowed", location);
		}
	}


}
