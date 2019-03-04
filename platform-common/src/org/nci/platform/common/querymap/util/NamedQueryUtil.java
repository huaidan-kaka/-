package org.nci.platform.common.querymap.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class NamedQueryUtil {
	
	public static JdbcSql toJdbcSql(String queryString, Map<String, Object> params){
		String sql = queryString;
		List<String> paramNameList = new ArrayList<String>();
		for(String p : params.keySet()){
			String pStr = "#{" + p + "}";
			int idx = -1;
			while((idx=sql.indexOf(pStr, idx+1)) >= 0){
				paramNameList.add(p);
			}
			sql = sql.replace(pStr, "?");
		}
		Object[] paramValues = new Object[paramNameList.size()];
		for(int i=0;i<paramNameList.size();i++){
			String pName = paramNameList.get(i);
			paramValues[i] = params.get(pName);
		}
		JdbcSql jdbcSql = new JdbcSql();
		jdbcSql.sql = sql;
		jdbcSql.params = paramValues;
		return jdbcSql;
	}
	
	public static String toHibernateHql(String queryString, Map<String, Object> params){
		String hql = queryString;
		for(String p : params.keySet()){
			String pStr = "#{" + p + "}";
			hql = hql.replace(pStr, ":"+p);
		}
		return hql;
	}
	
	static class JdbcSql{
		public String sql;
		public Object[] params;
	}
}
