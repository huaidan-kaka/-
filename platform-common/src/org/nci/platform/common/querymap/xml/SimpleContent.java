package org.nci.platform.common.querymap.xml;

import java.util.Map;

/**
 * 简单sql组成部分
 * 
 * @author zhangfuxue
 *
 */
public class SimpleContent implements IQueryContent {
	private String content;
	
	/**
	 * @param content
	 */
	public SimpleContent(String content){
		this.content = content;
	}

	@Override
	public String getContent(Map<String, Object> params) {
		return this.content;
	}
	
	@Override
	public String toString() {
		return this.content;
	}
	
}
