package org.nci.platform.common.querymap;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NamedQuery配置文件加载类
 * @author zhangfuxue
 *
 */
public class QueryLoader {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 查询配置文件
	 */
	private String[] queryConfigLoactions;
	// 是否每次获取statement都重新读取配置文件
	private boolean autoFlush = false;
	/**
	 * query语句map
	 */
	private Map<String, Map<String, NamedQuery>> queryMap;
	
	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}
	public boolean getAutoFlush() {
		return this.autoFlush;
	}
	public String[] getQueryConfigLoactions() {
		return queryConfigLoactions;
	}
	public void setQueryConfigLoactions(String[] queryConfigLoactions) {
		this.queryConfigLoactions = queryConfigLoactions;
	}
	
	//获取命名空间
	private String getNamespace(String queryId){
		int idx = queryId.lastIndexOf("#");
		if(idx < 0){
			return "";
		}else{
			return queryId.substring(0, idx);
		}
	}
	
	//获取除去命名空间的query name
	private String getQueryName(String queryId){
		int idx = queryId.lastIndexOf("#");
		if(idx < 0){
			return queryId;
		}else{
			return queryId.substring(idx + 1);
		}
	}
	/**
	 * 获取动态查询语句
	 * @param queryId ：<code>namespace#queryName</code>或者 <code>queryName</code>
	 * @param params
	 * @return
	 */
	public String getQueryString(String queryId, Map<String, Object> params){
		return getNamedQuery(queryId).getQueryString(params);
	}
	
	/**
	 * 根据query获取NamedQuery对象
	 * @param queryId ：<code>namespace#queryName</code>或者 <code>queryName</code>
	 * @return
	 */
	public NamedQuery getNamedQuery(String queryId){
		return getNamedQuery(getNamespace(queryId), getQueryName(queryId));
	}
	public NamedQuery getNamedQuery(String namespace, String queryName) {
		//开发期设置autoFlush为true，每次获取hql都重新加载文件，避免重启服务
		try {
			if(autoFlush){
				init();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap.get(namespace).get(queryName);
	}
	
	@SuppressWarnings("unchecked")
	public void init() throws IOException{
		logger.info("QueryLoader init()....");
		if(queryConfigLoactions == null || queryConfigLoactions.length<1){
			logger.warn("没有配置queryConfigLoacations");
			return;
		}
		queryMap = new HashMap<String, Map<String,NamedQuery>>();
		
		SAXBuilder builder = new SAXBuilder(false);
		for(String queryRes : queryConfigLoactions){
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(queryRes);
			try {
				Document doc = builder.build(is);
				
				Element root = doc.getRootElement();
				String namespace = root.getAttributeValue("namespace");
				if(namespace==null || namespace.trim().length()==0){
					namespace = "";
				}
				//读取query语句
				Map<String, NamedQuery> querys = null;
				if(queryMap.containsKey(namespace)){
					querys = queryMap.get(namespace);
				}else{
					querys = new HashMap<String, NamedQuery>();
					queryMap.put(namespace, querys);
				}
				List<Element> queryElems = root.getChildren("query");
				for(Element elem : queryElems){
					NamedQuery query = QueryCreator.createStatement(namespace, elem);
					querys.put(query.getName(), query);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally{
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		logger.info("namedQuerys:");
		for(String namespace : queryMap.keySet()){
			logger.info("namespace: "+namespace+"\t"+queryMap.get(namespace).keySet());
		}
		
	}
	
	
	
}