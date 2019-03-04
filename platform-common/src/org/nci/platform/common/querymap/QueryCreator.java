package org.nci.platform.common.querymap;

import java.util.LinkedList;
import java.util.List;

import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Text;
import org.nci.platform.common.querymap.util.XmlParseUtil;
import org.nci.platform.common.querymap.xml.DynamicContent;
import org.nci.platform.common.querymap.xml.IQueryContent;
import org.nci.platform.common.querymap.xml.SimpleContent;

/**
 * 动态sql解析器
 * @author zhangfuxue
 *
 */
public abstract class QueryCreator {
	/**
	 * 解析一个query标签
	 * @param namespace
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static NamedQuery createStatement(String namespace, Element element){
		String name = element.getAttributeValue("name");
		NamedQuery query = new NamedQuery(namespace, name);
		//获取query的属性
		query.setAttributes(XmlParseUtil.parseAttributes(element));
		//获取语句块
		List<Content> contents = element.getContent();
		List<IQueryContent> contentList = buildQueryContents(contents);
		query.setContentList(contentList);
		return query;
	}

	private static List<IQueryContent> buildQueryContents(List<Content> list) {
		List<IQueryContent> queryContentList = new LinkedList<IQueryContent>();
		for(Content content : list){
			if(content instanceof Text){
				Text simCon = (Text) content;
				String textCon = simCon.getTextTrim();
				if(textCon!=null && textCon.length()>0){
					queryContentList.add(new SimpleContent(textCon));
				}
			}else if(content instanceof Element){
				Element dynCon = (Element) content;
				queryContentList.add(parseContent(dynCon));
			}
		}
		return queryContentList;
	}
	/**
	 * 递归解析动态标签内容
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static IQueryContent parseContent(Element element) {
		String tagName = element.getName().toLowerCase();
		DynamicContent dynCon = DynamicContentFactory.getDynamicContent(tagName);
		//解析标签属性
		dynCon.parseAttrs(element);
		
		//解析子语句块
		List<Content> childs = element.getContent();
		List<IQueryContent> contentList = buildQueryContents(childs);
		dynCon.setContentList(contentList);
		return dynCon;
	}
}
