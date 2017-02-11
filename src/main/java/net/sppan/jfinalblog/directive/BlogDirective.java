package net.sppan.jfinalblog.directive;

import java.io.Writer;
import java.util.List;

import net.sppan.jfinalblog.service.BlogService;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Const;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.stat.Scope;

public class BlogDirective extends Directive{
	
	private final BlogService service = BlogService.me;

	private String type;
	@Override
	public void exec(Env env, Scope scope, Writer writer) {
		List<Record> list = service.findTopN(5,type);
		scope.setLocal("list", list);
		stat.exec(env, scope, writer);
	}

	@Override
	public void setExprList(ExprList exprList) {
		Expr[] exprs = exprList.getExprArray();
		if(exprs != null && exprs.length == 1){
			this.type = ((Const)exprs[0]).getStr();
		}
	}

	@Override
	public boolean hasEnd() {
		return true;
	}
	
	

}
