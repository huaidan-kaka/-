package org.nci.platform.common.querymap.xml;

import java.util.Map;

/**
 * sql语句的组成部分
 * 
 * @author zhangfuxue
 *
 */
public interface IQueryContent {
	/**
	 * 根据Map参数，获取该动态语句的实际内容
	 * @param params
	 * @return
	 */
	public String getContent(Map<String, Object> params);
}
