package org.nci.platform.common.querymap.xml;

import java.util.Map;

public class CaseContent extends DynamicContent {

	@Override
	protected String getTagName() {
		return "case";
	}

	@Override
	protected boolean isIncluded(Map<String, Object> params) {
		return true;
	}
	
	@Override
	public String getContent(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		if(params!=null && contentList!=null){
			for(IQueryContent content : contentList){
				if(DynamicContent.class.isAssignableFrom(content.getClass())){
					DynamicContent dynCon = (DynamicContent) content;
					String item = dynCon.getContent(params);
					if(item!=null){
						sb.append(item).append("\n");
					}
					if(dynCon.isIncluded(params)){
						break;
					}
				}
			}
		}
		
		return sb.toString();
	}
	
}
