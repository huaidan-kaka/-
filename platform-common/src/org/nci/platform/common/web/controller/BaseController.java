package org.nci.platform.common.web.controller;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.commons.lang3.math.NumberUtils;
import org.nci.platform.core.common.Consts;
import org.nci.platform.core.common.page.Page;
import org.nci.platform.core.common.result.Result;
import org.nci.platform.core.dao.IDao;
import org.nci.platform.core.service.IService;
import org.nci.platform.core.web.controller.AbstractSpringController;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangfl
 *
 * @param <T>
 * @param <Service>
 */
public abstract class BaseController<T, Service extends IService<T, ? extends IDao<T>>> extends AbstractSpringController<T, Service> {
	private SimpleDateFormat _sdf_ymd = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat _sdf_ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public Map<String, Object> getRequestParameters() {
		Map<String, Object> params = super.getRequestParameters();
		params.remove("startIndex");
		params.remove("pageSize");
		logger.debug("params:"+params);
		Set<String> keySet = new HashSet<String>(params.keySet());
		try {
			for(String key : keySet){
				Object valueObj = params.get(key);
				if(valueObj!=null && String.class.isAssignableFrom(valueObj.getClass()) && valueObj.toString().trim().length()>0){
					String value = valueObj.toString().trim();
					if(key.startsWith("int_")){
						params.remove(key);
						params.put(key.substring(4), Integer.parseInt(value));
					}else if(key.startsWith("date_")){
						params.remove(key);
						params.put(key.substring(5), _sdf_ymd.parse(value));
					}else if(key.startsWith("datetime_")){
						params.remove(key);
						params.put(key.substring(9), _sdf_ymdhms.parse(value));
					}else if(key.startsWith("ints_")){
						params.remove(key);
						String[] arr = value.split(",");
						Integer[] intArrs = new Integer[arr.length];
						for(int i=0;i<arr.length;i++){
							intArrs[i] = Integer.parseInt(arr[i]);
						}
						params.put(key.substring(5), intArrs);
					}else if(key.startsWith("double_")){
						params.remove(key);
						params.put(key.substring(7), Double.parseDouble(value));
					}
				}
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return params;
	}
	@RequestMapping("/get")
	public final void get(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = getRequestParameters();
		Object idObj = params.get("id");
		if(idObj==null){
			return;
		}
		String entityId = idObj.toString();
		Field idField = ReflectionUtils.findField(getEntityClass(), "id");
		
		Serializable id = null;
		if(Integer.class == idField.getType() || Long.class == idField.getType()){
			id = Integer.parseInt(entityId);
		}else{
			id = entityId;
		}
		T entity = getService().get(id);
		String result = this.getJsonConverter().toJsonString(entity, this.getConvertionConfig());
		this.getRender().render(response, result);
	}
	@RequestMapping("/services/get")
	public final void getForApp(HttpServletRequest request,
			HttpServletResponse response) {
		get(request, response);
	}
	@RequestMapping("/services/queryUnique")
	public final void queryUniqueForApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = getRequestParameters();
		List<T> list = doQueryForApp(params);
		T entity = null;
		if(list.size()>0){
			entity = list.get(0);
		}
		String result = this.getJsonConverter().toJsonString(entity, this.getConvertionConfig());
		this.getRender().render(response, result);
	}
	@RequestMapping("/services/query")
	public final void queryForApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = getRequestParameters();
		List<T> list = doQueryForApp(params);
		String result = this.getJsonConverter().toJsonString(list, this.getConvertionConfig());
		this.getRender().render(response, result);
	}
	@RequestMapping("/services/querypage")
	public final void queryPageForApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = getRequestParameters();
		int startIndex = NumberUtils.toInt(this.getRequestContext().getRequest().getParameter("startIndex"), 0);
		int pageSize = NumberUtils.toInt(this.getRequestContext().getRequest().getParameter("pageSize"), Consts.DEFAULT_PAGE_SIZE);
		Page page = doQueryPageForApp(startIndex, pageSize, params);
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put(Consts.PAGE_ENTITY_TOTAL, ((Page) page).getTotalCount());
		pageMap.put(Consts.PAGE_ENTITY_DATA, ((Page) page).getRecords());
		String result = this.getJsonConverter().toJsonString(pageMap, this.getConvertionConfig());
		this.getRender().render(response, result);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/services/save")
	public final void saveForApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> params = getRequestParameters();
		Result result = newResult();
		Object entity = getDefaultEntityData();
		// convention validate
		if (null != entity) {
			if (isCollection(entity)) {
				this.getValidator().validateAll((Collection<?>) entity, result);
				validateSaveForApp((Collection<T>) entity, params, result);
			} else {
				this.getValidator().validate(entity, result);
				validateSaveForApp((T) entity, params, result);
			}
		} else {
			result.addErrorCode("default.none.element", null, "没有数据！");
		}
		// do really handle
		if (result.isSuccess()) {
			try {
				if (isCollection(entity)) {
					doSaveForApp((Collection<T>) entity, params, result);
				} else {
					doSaveForApp((T) entity, params, result);
				}
			} catch (Exception e) {
				this.getHandleException().handleException(result, e);
				logger.error(e.getMessage());
			}
		}
		// response
		this.getRender().render(response, result);
	}
	
	/**
	 * 查询操作, 子类可通过复写该方法定制查询操作<br/>
	 * 查询结果以Page对象形式返回
	 * 
	 * @param params
	 *            request请求中的参数集合
	 * @param pageIndex
	 *            当前页面索引
	 * @param pageSize
	 *            分布大小
	 */
	protected Page doQueryPageForApp(int startIndex, int pageSize,
			Map<String, Object> params) {
		Page page = this.getService().queryForAppPage(startIndex, pageSize, params);
		return page;
	}
	
	/**
	 * 查询操作, 子类可通过复写该方法定制查询操作<br/>
	 * 查询结果以列表形式返回
	 * 
	 * @param params
	 *            request请求中的参数集合
	 */
	protected List<T> doQueryForApp(Map<String, Object> params) {
		List<T> list = this.getService().query(params);
		return list;
	}
	/**
	 * 保存操作, 子类可通过复写该方法定制保存操作
	 * 
	 * @param entities
	 *            需要保存的实体对象集合
	 * @param params
	 *            request请求中的参数集合
	 * @param result
	 *            返回结果对象
	 */
	public void doSaveForApp(Collection<T> entities, Map<String, Object> params,
			Result result) throws Exception {
		this.getService().save(entities);
		result.addMessage(this.getMessageSource()
				.getMessage("default.save.success", null));
	}
	/**
	 * 保存操作, 子类可通过复写该方法定制保存操作
	 * 
	 * @param entity
	 *            需要保存的实体对象
	 * @param params
	 *            request请求中的参数集合
	 * @param result
	 *            返回结果对象
	 */
	public void doSaveForApp(T entity, Map<String, Object> params, Result result)
			throws Exception {
		this.getService().save(entity);
		result.addMessage(this.getMessageSource()
				.getMessage("default.save.success", null));
	}
	/**
	 * 自定义校验Save操作，需子类使用时实现
	 * 
	 * @param entities
	 *            需要保存的实体对象集合
	 * @param params
	 *            request请求中的参数集合
	 * @param result
	 *            返回结果对象
	 */
	protected void validateSaveForApp(Collection<T> entities,
			Map<String, Object> params, Result result) {
	}
	/**
	 * 自定义校验Save操作，需子类使用时实现
	 * 
	 * @param entity
	 *            需要保存的实体对象
	 * @param params
	 *            request请求中的参数集合
	 * @param result
	 *            返回结果对象
	 */
	protected void validateSaveForApp(T entity, Map<String, Object> params,
			Result result) {
	}
}
