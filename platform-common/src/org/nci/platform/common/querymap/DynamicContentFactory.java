package org.nci.platform.common.querymap;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nci.platform.common.querymap.xml.CaseContent;
import org.nci.platform.common.querymap.xml.DynamicContent;
import org.nci.platform.common.querymap.xml.ElseContent;
import org.nci.platform.common.querymap.xml.EmptyContent;
import org.nci.platform.common.querymap.xml.EqualsContent;
import org.nci.platform.common.querymap.xml.IFContent;
import org.nci.platform.common.querymap.xml.IQueryContent;
import org.nci.platform.common.querymap.xml.NotEmptyContent;
import org.nci.platform.common.querymap.xml.NotEqualsContent;

/**
 * 动态查询语句管理器
 * @author zhangfuxue
 *
 */
public class DynamicContentFactory {
	private static Map<String, Class<? extends IQueryContent>> map = new HashMap<String, Class<? extends IQueryContent>>();
	private static Logger logger = LoggerFactory.getLogger(DynamicContentFactory.class);
	static{
		map.put("if", IFContent.class);
		map.put("case", CaseContent.class);
		map.put("else", ElseContent.class);
		map.put("empty", EmptyContent.class);
		map.put("notempty", NotEmptyContent.class);
		map.put("equals", EqualsContent.class);
		map.put("notequals", NotEqualsContent.class);
	}
	
	private Map<String, String> contentMap;
	
	public void setContentMap(Map<String, String> contentMap) {
		this.contentMap = contentMap;
	}
	
	@SuppressWarnings("unchecked")
	public void init(){
		if(contentMap ==null || contentMap.size() <= 0){
			return;
		}
		try{
			for(String key : contentMap.keySet()){
				map.put(key.toLowerCase(), (Class<? extends IQueryContent>) Class.forName(contentMap.get(key)));
			}
		}catch(ClassNotFoundException e){
			throw new IllegalArgumentException(e);
		}
		logger.info("init()....contentMap:"+map);
	}
	
	public static DynamicContent getDynamicContent(String tagName){
		try {
			return (DynamicContent) map.get(tagName).newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("无效标签："+tagName+"\t"+e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
