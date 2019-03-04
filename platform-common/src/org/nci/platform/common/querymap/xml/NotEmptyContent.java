package org.nci.platform.common.querymap.xml;

import java.util.Map;
/**
 * 非空动态查询语句块（当参数值非空时返回查询语句，，否则返回“”）
 * <pre><code>
 * &lt;notempty name="xxx"> statment &lt;/equals>
 * </code></pre>
 * @author zhangfuxue
 *
 */
public class NotEmptyContent extends DynamicContent {

	@Override
	protected String getTagName() {
		return "notempty";
	}

	@Override
	protected boolean isIncluded(Map<String, Object> params) {
		String paramName = attributes.get(PARAM_NAME_KEY);
		Object paramValue = params.get(paramName);
		return paramValue!=null && paramValue.toString().length()>0;
	}

}
