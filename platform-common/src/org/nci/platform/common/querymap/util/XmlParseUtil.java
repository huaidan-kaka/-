package org.nci.platform.common.querymap.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Element;
/**
 * xml 解析工具类
 * @author zhangfuxue
 *
 */
public abstract class XmlParseUtil {
	/**
	 * 获取一个标签的所有属性，封装成Map返回
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseAttributes(Element element){
		List<Attribute> attrs = element.getAttributes();
		Map<String, String> attrsMap = new HashMap<String, String>();
		for(Attribute attr : attrs){
			attrsMap.put(attr.getName(), attr.getValue());
		}
		return attrsMap;
	}
}
