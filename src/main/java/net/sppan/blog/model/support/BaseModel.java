package net.sppan.blog.model.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

public abstract class BaseModel<M extends Model<M>> extends Model<M> {
	
	private static final long serialVersionUID = -8686483734298113781L;
	private Logger logger = Logger.getLogger(getClass());  
	
	public List<M> getAllList() {
		Page<M> page= this.getPage(1, Integer.MAX_VALUE);
		return page.getList();
	}
	public List<M> getList(int pageNumber, int pageSize) {
		Page<M> page= this.getPage(pageNumber, pageSize);
		return page.getList();
	}
	public List<M> getList(LinkedHashMap<String, String> orderby) {
		Page<M> page= this.getPage(1, Integer.MAX_VALUE,null,orderby);
		return page.getList();
	}
	public List<M> getList(int pageNumber, int pageSize,Set<Condition> conditions) {
		Page<M> page= this.getPage(pageNumber, pageSize,conditions);
		return page.getList();
	}
	public List<M> getList(int pageNumber, int pageSize,LinkedHashMap<String, String> orderby) {
		Page<M> page= this.getPage(pageNumber, pageSize,null,orderby);
		return page.getList();
	}
	public List<M> getList(int pageNumber, int pageSize,Set<Condition> conditions,LinkedHashMap<String, String> orderby) {
		Page<M> page= this.getPage(pageNumber, pageSize,conditions,orderby);
		return page.getList();
	}
	public List<M> getList(int pageNumber, int pageSize,Set<Condition> conditions,LinkedHashMap<String, String> orderby,String... propertys) {
		Page<M> page= this.getPage(pageNumber, pageSize,conditions,orderby,propertys);
		return page.getList();
	}
	/**
	 * 持久化再返回该对象
	 * @author eason	
	 * @return
	 */
	public M persist(){
		this.save();
		return (M)this;
	}
	public Page<M> getPage(int pageNumber, int pageSize){
		return this.paginate(pageNumber, pageSize, "select *", "from "+getTableName());
	}
	public Page<M> getPage(int pageNumber, int pageSize,
			Set<Condition> conditions) {
		return this.getPage(pageNumber, pageSize, conditions,null);
	}
	public Page<M> getPage(int pageNumber, int pageSize,
			LinkedHashMap<String, String> orderby) {
		return this.getPage(pageNumber, pageSize, null,orderby);
	}
	public Page<M> getPage(int pageNumber, int pageSize,
			Set<Condition> conditions, LinkedHashMap<String, String> orderby) {
		
		return this.getPage(pageNumber, pageSize, conditions, orderby,"");
	}
	public Page<M> getPage(int pageNumber, int pageSize,
			Set<Condition> conditions, LinkedHashMap<String, String> orderby,
			String... propertys) {
		List<Object> outConditionValues=new ArrayList<Object>();
		String sqlExceptSelect="from "+getTableName()+this.getWhereSql(conditions, outConditionValues)+this.getOrderbySql(orderby);
		return this.paginate(pageNumber, pageSize, this.getSelectSQL(propertys), sqlExceptSelect, outConditionValues.toArray());
	}
	public Page<M> getPage(int pageNumber, int pageSize,
			String selectSql, String sqlExceptSelect,LinkedHashMap<String, String> orderby,
			Object... paras) {
		sqlExceptSelect=sqlExceptSelect+this.getOrderbySql(orderby);
		return this.paginate(pageNumber, pageSize,selectSql, sqlExceptSelect, paras);
	}
	
	public int update(Set<Condition> conditions, Map<String, Object> newValues) {
		if (newValues != null && newValues.size() > 0) {
			StringBuilder buf = new StringBuilder("update ");
			buf.append(this.getTableName()).append(" ").append("set  ");
			Iterator<Entry<String, Object>> ite = newValues.entrySet()
					.iterator();
			List<Object> outConditionValues=new ArrayList<Object>();
			while (ite.hasNext()) {
				Entry<String, Object> en = ite.next();
				buf.append(en.getKey()).append("=")
						.append(" ? ");
				outConditionValues.add(en.getValue());
				if (ite.hasNext()) {
					buf.append(",");
				}
			}
			buf.append(this.getWhereSql(conditions, outConditionValues));
			return Db.update(buf.toString(), outConditionValues.toArray());
		}
		return 0;
	}
	public int delete(Set<Condition> conditions) {
		List<Object> outConditionValues=new ArrayList<Object>();
		String sql="delete from "+getTableName()+this.getWhereSql(conditions, outConditionValues);
		return Db.update(sql, outConditionValues.toArray());
	}
	
	public Long getCount(){
		return this.getCount(null);
	}
	public Long getCount(Set<Condition> conditions) {
		List<Object> outConditionValues=new ArrayList<Object>();
		String sql="select count(*) from "+getTableName()+this.getWhereSql(conditions, outConditionValues);
		return Db.queryLong(sql, outConditionValues.toArray());
	}
	public M get(Set<Condition> conditions, String... propertys) {
		List<Object> outConditionValues=new ArrayList<Object>();
		String sqlExceptSelect=" from "+getTableName()+this.getWhereSql(conditions, outConditionValues);
		return this.findFirst(this.getSelectSQL(propertys)+sqlExceptSelect, outConditionValues.toArray());
	}
	public M get(Set<Condition> conditions) {
		List<Object> outConditionValues=new ArrayList<Object>();
		String sql="select * from "+getTableName()+this.getWhereSql(conditions, outConditionValues);
		return this.findFirst(sql, outConditionValues.toArray());
	}
	public boolean isExit(Set<Condition> conditions) {
		return getCount(conditions)>0l?true:false;
	}
	public String getTableName(){
		Table tableInfo = TableMapping.me().getTable(clazz);
		return tableInfo.getName();
	}
	
	private Class<M> clazz = getThisClass(getClass());
	
	private Class<M> getThisClass(@SuppressWarnings("rawtypes") Class clazz) {
		Type type = clazz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] ts = ((ParameterizedType) type).getActualTypeArguments();
			return (Class<M>) ts[0];
		}
		throw new RuntimeException("继承出现错误！父类需要使用泛型却没有使用泛型。。");
	}
	/**
	 * 得到排序SQL语句
	 * @param orderbys
	 * @return
	 */
	private String getOrderbySql(LinkedHashMap<String, String> orderbys) {
		if (orderbys == null || orderbys.size() < 1) {
			return "";
		}
		StringBuilder buf = new StringBuilder(" order by  ");
		Set<Entry<String, String>> entrySet = orderbys.entrySet();
		Iterator<Entry<String, String>> ite = entrySet.iterator();
		while (ite.hasNext()) {
			Entry<String, String> en = ite.next();
			buf.append(en.getKey()).append("  ")
					.append(en.getValue());
			if (ite.hasNext()) {
				buf.append(",");
			}
		}
		return buf.toString();
	}
	/**
	 * 得到SQL语句的select部分语句不包括WHERE部分
	 */
	private String getSelectSQL(String... propertys) {
		StringBuilder buf = new StringBuilder(" select  ");
		if (propertys != null && propertys.length > 0
				&& !propertys[0].trim().equals("")) {
			for (int i = 0; i < propertys.length; i++) {
				buf.append(propertys[i]);
				if (i < propertys.length - 1) {
					buf.append(",");
				}
			}
		}else{
			buf.append(" * ");
		}
		return buf.append("  ").toString();
	}
	
	/**
	 * 拼接 SQL Where 语句
	 * @param conditions
	 * @return
	 */
	private String getWhereSql(Set<Condition> conditions,List<Object> outConditionValues) {
		 
		if (conditions != null && conditions.size() > 0) {
			StringBuilder bufWhere = new StringBuilder(" where  ");
			Iterator<Condition> ite = conditions.iterator();
			while (ite.hasNext()) {
				// 得到条件数据对象
				Condition cd = ite.next();
				// 拼接属性
				bufWhere.append(cd.getProperty());
				// 判断是否需要做为空特殊处理
				boolean isnull = cd.getValue() == null
						|| cd.getValue().toString().trim().equals("");
				// 开始判断
				if (cd.getOper().equals(Operators.EQ)) {
					if (isnull) {
						bufWhere.append(" is  null  ");
					} else {
						bufWhere.append("=");
						outConditionValues.add(cd.getValue());
					}

				} else if (cd.getOper().equals(Operators.NOT_EQ)) {
					if (isnull) {
						bufWhere.append("  is not null  ");
					} else {
						bufWhere.append("<>");
						outConditionValues.add(cd.getValue());
					}
				} else if (cd.getOper().equals(Operators.LE)) {
					bufWhere.append("<=");
					outConditionValues.add(cd.getValue());
				} else if (cd.getOper().equals(Operators.LT)) {
					bufWhere.append("<");
					outConditionValues.add(cd.getValue());
				} else if (cd.getOper().equals(Operators.GE)) {
					bufWhere.append(">=");
					outConditionValues.add(cd.getValue());
				} else if (cd.getOper().equals(Operators.GT)) {
					bufWhere.append(">");
					outConditionValues.add(cd.getValue());
				} else if (cd.getOper().equals(Operators.IN)) {
					if(cd.getValue() instanceof List){
						StringBuilder ids=new StringBuilder();
						for(Object item : (List)cd.getValue()){
							if(item instanceof String){
								ids.append("'").append(item).append("'").append(",");
							}else{
								ids.append(item).append(","); 
							}
						}
						if(ids.length()>0){ids.deleteCharAt(ids.length()-1);}
						bufWhere.append(" in ("+ids.toString()+")");
					}else{
						outConditionValues.add(cd.getValue());
					}
					
				} else if (cd.getOper().equals(Operators.NOT_IN)) {
					if(cd.getValue() instanceof List){
						StringBuilder ids=new StringBuilder();
						for(Object item : (List)cd.getValue()){
							if(item instanceof String){
								ids.append("'").append(item).append("'").append(",");
							}else{
								ids.append(item).append(","); 
							}
						}
						if(ids.length()>0){ids.deleteCharAt(ids.length()-1);}
						bufWhere.append(" not in ("+ids.toString()+")");
					}else{
						outConditionValues.add(cd.getValue());
					}
				} else if (cd.getOper().equals(Operators.LIKE)) {
					bufWhere.append(" like '%"+cd.getValue()+"%'");
				} else if (cd.getOper().equals(Operators.BETWEEN)) {
					bufWhere.append("  between  ").append(" ? ").append("  and  ").append(
									" ? ");
					outConditionValues.add(cd.getFirstValue());
					outConditionValues.add(cd.getValue());
				}

				if (!cd.getOper().equals(Operators.LIKE)&&!cd.getOper().equals(Operators.BETWEEN)
						&& !cd.getOper().equals(Operators.IN)&&!cd.getOper().equals(Operators.NOT_IN)) {
					if (!isnull) {
						bufWhere.append(" ? ");
					}
				}
				if (ite.hasNext()) {
					bufWhere.append(" and  ");
				}
			}
			return bufWhere.toString();
		} else {
			return "";
		}
	}
}
