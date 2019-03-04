package org.nci.platform.common.querymap.xml;

import java.util.Map;

/**
 * 动态语句块－－判断参数值等于某值
 * <pre><code>
 * &lt;equals name="type" value="dw"> statement &lt;/equals>
 * </code></pre>
 * @author zhangfuxue
 *
 */
public class EqualsContent extends DynamicContent {

	@Override
	protected String getTagName() {
		return "equals";
	}

	@Override
	protected boolean isIncluded(Map<String, Object> params) {
		String paramName = attributes.get(PARAM_NAME_KEY);
		Object paramValue = params.get(paramName);
		String value = attributes.get(PARAM_VALUE_KEY);
		return value!=null && value.equals(paramValue);
	}

}
