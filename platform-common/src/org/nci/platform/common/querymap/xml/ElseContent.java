package org.nci.platform.common.querymap.xml;

import java.util.Map;
/**
 * else查询条件，用于case查询条件中，等上边条件都不成立时，返回该查询语句
 * @author zhangfuxue
 *
 */
public class ElseContent extends DynamicContent {

	@Override
	protected String getTagName() {
		return "else";
	}

	@Override
	protected boolean isIncluded(Map<String, Object> params) {
		return true;
	}

}
