package org.nci.platform.common.querymap.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DynamicContent implements IQueryContent {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String PARAM_NAME_KEY = "name";
	public static final String PARAM_VALUE_KEY = "value";
	
	/**
	 * 动态标签的属性
	 */
	protected Map<String, String> attributes;
	
	/**
	 * 该动态语句的组成部分
	 */
	protected List<IQueryContent> contentList;
	
	public DynamicContent(){}
	/**
	 * 解析标签属性
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public final void parseAttrs(Element element){
		attributes = new HashMap<String, String>();
		List<Attribute> attrs = element.getAttributes();
		for(Attribute attr : attrs){
			attributes.put(attr.getName(), attr.getValue());
		}
	}
	public void setContentList(List<IQueryContent> contentList){
		this.contentList = contentList;
	}
	/**
	 * 返回该动态语句的标签名称，由子类实现
	 * @return
	 */
	protected abstract String getTagName();
	
	/**
	 * 根据参数值来判断该动态语句是否应该包含，由子类实现
	 */
	protected abstract boolean isIncluded(Map<String, Object> params);
	
	
	@Override
	public String getContent(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		if(params!=null && contentList!=null && isIncluded(params)){
			for(IQueryContent content : contentList){
				String item = content.getContent(params);
				if(item != null){
					sb.append(item).append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<" + getTagName());
		for(String attr : attributes.keySet()){
			sb.append(" "+attr+"=\""+this.attributes.get(attr)+"\"");
		}
		sb.append(" >\n");
		if(contentList != null){
			for(IQueryContent content : contentList){
				sb.append(content.toString()).append("\n");
			}
		}
		sb.append("</" + getTagName() + ">");
		return sb.toString();
	}

}
