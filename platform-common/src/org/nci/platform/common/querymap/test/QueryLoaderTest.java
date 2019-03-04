package org.nci.platform.common.querymap.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.nci.platform.common.querymap.QueryLoader;

public class QueryLoaderTest {

	public static void main(String[] args) throws IOException {
		QueryLoader queryLoader = buildQueryLoader();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("param1", "value1");
		params.put("param2", "value2");
		params.put("param3", "value3");
//		params.put("param4", "value4");
		String queryString = queryLoader.getQueryString("org.test#query_test", params);
		System.out.println(queryString);
		
	}

	private static QueryLoader buildQueryLoader() throws IOException {
		QueryLoader queryLoader = new QueryLoader();
		String[] queryConfigLoactions = new String[]{"org/nci/platform/common/querymap/test/demo_queries.xml"};
		queryLoader.setQueryConfigLoactions(queryConfigLoactions);
		queryLoader.init();
		return queryLoader;
	}

}
