package org.nci.platform.common.querymap.xml;

import java.util.Map;

/**
 * 动态语句块－－判断参数值不等于某值
 * <pre><code>
 * &lt;notequals name="type" value="dw"> statement &lt;/notequals>
 * </code></pre>
 * @author zhangfuxue
 *
 */
public class NotEqualsContent extends DynamicContent {

	@Override
	protected String getTagName() {
		return "notequals";
	}

	@Override
	protected boolean isIncluded(Map<String, Object> params) {
		String paramName = attributes.get(PARAM_NAME_KEY);
		Object paramValue = params.get(paramName);
		String value = attributes.get(PARAM_VALUE_KEY);
		return value==null || !value.equals(paramValue);
	}

}
