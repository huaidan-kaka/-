package org.nci.platform.common.querymap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nci.platform.common.querymap.xml.IQueryContent;

/**
 * 查询语句bean
 * @author zhangfuxue
 *
 */
public class NamedQuery {
	private String namespace;
	private String name;
	private Map<String, String> attributes;
	private List<IQueryContent> contentList;
	
	public NamedQuery(String namespace, String name){
		this.namespace = namespace;
		this.name = name;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public void setContentList(List<IQueryContent> contentList) {
		this.contentList = contentList;
	}
	/**
	 * 根据实际参数，动态生成查询语句
	 * @param params
	 * @return
	 */
	public String getQueryString(Map<String, Object> params){
		return getQueryString(params, false);
	}
	/**
	 * 根据实际参数，动态生成查询语句
	 * @param params
	 * @param removeUnusedParams 如果为true，则从params中移除最终生成的语句中未用到的参数 :param
	 * @return
	 */
	public String getQueryString(Map<String, Object> params, boolean removeUnusedParams) {
		if(params == null){
			params = new HashMap<String, Object>();
		}
		StringBuilder sb = new StringBuilder();
		for(IQueryContent content : contentList){
			String item = content.getContent(params);
			if(item != null){
				sb.append(item).append("\n");
			}
		}
		String sql = sb.toString();
		
		//对于参数 ${param}直接在生成语句时替换成参数值
		int idx;
		while((idx = sql.indexOf("${")) >= 0){
			int end = sql.indexOf("}", idx);
			String pname = sql.substring(idx+2, end).trim();
			String value = params.get(pname) == null ? "" : params.get(pname).toString();
			sql = sql.replace(sql.substring(idx, end+1), value);
			
			//删除无用的参数
			if(removeUnusedParams){
				List<String> unUsedParam = new LinkedList<String>();
				for(String param : params.keySet()){
					if(sql.indexOf(":"+param) < 0){
						unUsedParam.add(param);
					}
				}
				for(String p : unUsedParam){
					params.remove(p);
				}
			}
		}
		return sql.replaceAll("\\s+", " ");
	}
	
	/**
	 * 获取属性
	 * @param attrName
	 * @return
	 */
	public String getAttribute(String attrName){
		return attributes.get(attrName);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query name=\""+this.name+"\" ");
		for(String attr : attributes.keySet()){
			sb.append(attr + "=\"" + getAttribute(attr) + "\" ");
		}
		sb.append("\n");
		for(IQueryContent content : contentList){
			sb.append(content.toString()).append("\n");
		}
		sb.append("</query>\n");
		return sb.toString();
	}

	public String getNamespace() {
		return namespace;
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
}
