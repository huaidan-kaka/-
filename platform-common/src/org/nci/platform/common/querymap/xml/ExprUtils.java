package org.nci.platform.common.querymap.xml;
/**
 * if查询语句中支持的函数集合
 * 
 * @author zhangfuxue
 *
 */
public class ExprUtils {
	public boolean empty(String s){
		return s==null || s.length()==0;
	}
	public boolean notEmpty(String s){
		return !empty(s);
	}
	public boolean allEmpty(String...strs){
		if(strs == null){
			return true;
		}else{
			for(String s : strs){
				if(notEmpty(s)){
					return false;
				}
			}
			return true;
		}
	}
	public boolean anyEmpty(String...strs){
		if(strs == null){
			return false;
		}else{
			for(String s : strs){
				if(empty(s)){
					return true;
				}
			}
			return false;
		}
	}
	public boolean allNotEmpty(String...strs){
		return !anyEmpty(strs);
	}
	public boolean anyNotEmpty(String...strs){
		return !allEmpty(strs);
	}
	public boolean equals(String s, Object v){
		return s != null && s.equals(v);
	}
}
