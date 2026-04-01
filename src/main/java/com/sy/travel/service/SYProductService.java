package com.sy.travel.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.common.ResultBuilder;
import com.sy.travel.common.Commons;
import com.sy.travel.common.DateFormat;
import com.sy.travel.dao.SYClassesRepository;
import com.sy.travel.dao.SYProductRepository;
import com.sy.travel.entity.Logger;
import com.sy.travel.entity.Product;
import com.sy.travel.utils.JSON;

/**
 * 产品信息的服务层处理
 * @author liuxin
 *
 */
@Service
public class SYProductService implements DateFormat{
	@Autowired
	private SYProductRepository syProductRepository;
	@Autowired
	private SYTeamService syTeamService;
	@Autowired
	private SYClassesRepository syClassesRepository;
	@Autowired
	private SYLoggerService syLoggerService;

	/**
	 * 添加产品信息
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> add(JSON json, String operator) {
		operator = (String) json.get("operator");
		Date start = new Date();
		String reason = "";
		String code = (String) json.get("code");
		if(StringUtils.isBlank(code)) {
			reason = Commons.PRODUCT_ADD_CODE_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		Product regexCode = syProductRepository.findByCode(code);
		if(regexCode != null) {
			reason = Commons.PRODUCT_ADD_CODE_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		String name = (String) json.get("name");
		if(StringUtils.isBlank(name)) {
			reason = Commons.PRODUCT_ADD_NAME_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		Product regexName = syProductRepository.findByName(name);
		if(regexName != null) {
			reason = Commons.PRODUCT_ADD_NAME_IS_EXISTS;
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		if(StringUtils.isBlank((String)json.get("teamId")) || syTeamService.findOne(Integer.valueOf((String) json.get("teamId"))) == null) {
			reason = Commons.PRODUCT_ADD_TEAM_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		int teamId = Integer.valueOf((String) json.get("teamId"));
		String exText = StringUtils.isBlank((String) json.get("exText")) ? "" : (String) json.get("exText");
		String onlineDate = (String) json.get("onlineDate");
		String offlineDate = (String) json.get("offlineDate");
		if(StringUtils.isBlank(onlineDate) || StringUtils.isBlank(offlineDate)) {
			reason = Commons.PRODUCT_ADD_DATE_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		Date offDate = null;
		try {
			offDate = sdf.parse(offlineDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date onDate = null;
		try {
			onDate = sdf.parse(onlineDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(offDate.before(onDate)) {
			reason = Commons.PRODUCT_ADD_DATE_BEFORE;
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		if(StringUtils.isBlank((String)json.get("classId")) || syClassesRepository.findOne(Integer.valueOf((String)json.get("classId"))) == null) {
			reason = Commons.PRODUCT_ADD_CLASSES_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		int quantity = (int) json.get("quantity");
		int minQty = (int) json.get("minQty");
		int soldQty = (int) json.get("soldQty");
		int price = (int) json.get("price");
		int classId = Integer.valueOf((String)json.get("classId"));
		int nights = (int) json.get("nights");
		String status = (String) json.get("status");
		if(StringUtils.isBlank(status)) {
			reason = "产品状态不能为空";
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		String remark = StringUtils.isBlank((String) json.get("remark")) ? "" : (String) json.get("remark");
		Product product = new Product(code, name, teamId, onlineDate, offlineDate, quantity, minQty, soldQty, price, classId, nights, status, remark, operator, sdf.format(new Date()));
		product.setModifiedDate("");
		product.setModifiedUser("");
		product.setExText(exText);
		Product resP = syProductRepository.save(product);
		if(resP == null) {
			reason = "产品信息保存到数据库失败";
			return result(operator, start, reason, "0", Commons.PRODUCT_ADD);
		}
		return result(operator, start, reason, "1", Commons.PRODUCT_ADD);
	}
	
	/**
	 * 删除产品信息
	 * @param id
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> delete(int id, String operator){
		Date start = new Date();
		String reason = "";
		try {
			syProductRepository.delete(id);
			return result(operator, start, reason, "1", Commons.PRODUCT_DELETE);
		} catch (Exception e) {
			reason = "产品id不存在或删除失败";
			return result(operator, start, reason, "0", Commons.PRODUCT_DELETE);
		}
	}
	

	
	/**
	 * 更新产品信息
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> update(JSON json, String operator){
		operator = (String) json.get("operator");
		Date start = new Date();
		String reason = "";
		int id = (int) json.get("id");
		Product product = syProductRepository.findOne(id);
		if(product == null) {
			reason = id + Commons.PRODUCT_DELETE_ID_NOT_FOUND;
			return result(operator, start, reason, "0", Commons.PRODUCT_DELETE);
		}
		String name = (String) json.get("name");
		if(StringUtils.isBlank(name)) {
			name = product.getName();
		} else {
			Product regexName = syProductRepository.findByName(name);
			if(regexName != null && regexName.getId() != id) {
				reason = Commons.PRODUCT_ADD_NAME_IS_EXISTS;
				return result(operator, start, reason, "0", Commons.PRODUCT_DELETE);
			}
		}
		String exText = StringUtils.isBlank((String) json.get("exText")) ? product.getExText() : (String) json.get("exText");
		int quantity = (int) json.get("quantity");
		int minQty = (int) json.get("minQty");
		int soldQty = (int) json.get("soldQty");
		int price = (int) json.get("price");
		int nights = (int) json.get("nights");
		String status = (String) json.get("status");
		String remark = StringUtils.isBlank((String) json.get("remark")) ? "" : (String) json.get("remark");
		Product product1 = new Product(product.getCode(), name, product.getTeamId(), product.getOnlineDate(), product.getOfflineDate(), quantity, minQty, soldQty, price, product.getClassId(), nights, status, remark, product.getCreatedUser(), product.getCreatedDate());
		product1.setModifiedDate(sdf.format(new Date()));
		product1.setId(id);
		product1.setModifiedUser(operator);
		product1.setExText(exText);
		Product resP = syProductRepository.save(product1);
		if(resP == null) {
			reason = "产品信息更新失败";
			return result(operator, start, reason, "0", Commons.PRODUCT_DELETE);
		}
		return result(operator, start, reason, "1", Commons.PRODUCT_DELETE);
	}
	
	/**
	 * 查看所有的产品信息
	 * 当name不为空时模糊查询
	 * @param name
	 * @return
	 */
	public Map<String, Object> findAll(String name, int currentPage, int pageSize) {
		Page<Product> list = null;
		Sort sort = new Sort(Direction.DESC, "onlineDate");
		Pageable page = new PageRequest(currentPage-1, pageSize, sort);
		long count = 0;
		if(!StringUtils.isBlank(name)) {
			name = "%" + name + "%";
			list = syProductRepository.findByNameLike(name, page);
			count = syProductRepository.countByNameLike(name);
		} else {
			list = syProductRepository.findAll(page);
			count = syProductRepository.count();
		}
		Map<String, Object> resultMap = new HashMap<>();
		if (list == null || list.getSize() == 0) {
			resultMap.put("total", count);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> tempList = new ArrayList<>();
		for (Product product : list) {
			Map<String, Object> temp = new HashMap<>(product.toMap());
			temp.put("tname", syTeamService.findOne(product.getTeamId()).getName());
			temp.put("cname", syClassesRepository.findOne(product.getClassId()).getName());
			tempList.add(temp);
		}
		resultMap.put("total", count);
		resultMap.put("documents", tempList);
		return resultMap;
	}
	
	/**
	 * 通过团队id查看这个团队下所有产品的信息
	 * @param teamId
	 * @return
	 */
	public Map<String, Object> findByTeamId(String id) {
		if(StringUtils.isBlank(id)) {
			return findAll("", 1, Integer.MAX_VALUE);
		}
		Map<String, Object> resultMap = new HashMap<>();
		Integer teamId = Integer.valueOf(id);
		List<Product> queryList = syProductRepository.findByTeamId(teamId);
		if(queryList == null || queryList.size() == 0) {
			resultMap.put("total", 0);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> teampList = new ArrayList<>();
		for (Product product : queryList) {
			Map<String, Object> teap = new HashMap<>(product.toMap());
			teap.put("tname", syTeamService.findOne(product.getTeamId()).getName());
			//修改分类的名称
			teap.put("cname", syClassesRepository.findOne(product.getClassId()).getName());
			teampList.add(teap);
		}
		resultMap.put("total", teampList.size());
		resultMap.put("documents", teampList);
		return resultMap;
	}
	
	/**
	 * 通过分类id查看这个分类下所有产品的信息
	 * 当id没有传入时，查询所有的产品信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> findByClassId(String id) {
		if(StringUtils.isBlank(id)) {
			return findAll("", 1, Integer.MAX_VALUE);
		}
		Map<String, Object> resultMap = new HashMap<>();
		Integer classId = Integer.valueOf(id);
		List<Product> queryList = syProductRepository.findByClassId(classId);
		if(queryList == null || queryList.size() == 0) {
			resultMap.put("total", 0);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> teampList = new ArrayList<>();
		for (Product product : queryList) {
			Map<String, Object> teap = new HashMap<>(product.toMap());
			teap.put("tname", syTeamService.findOne(product.getTeamId()).getName());
			teap.put("cname", syClassesRepository.findOne(product.getClassId()).getName());
			teampList.add(teap);
		}
		resultMap.put("total", teampList.size());
		resultMap.put("documents", teampList);
		return resultMap;
	}
	
	/**
	 * 对产品模块操作结果的统一处理
	 * 
	 * @param operator
	 *            操作者
	 * @param start
	 *            开始时间
	 * @param reason
	 *            为空，则操作成功；不为空，则记录操作失败原因
	 * @param status
	 *            记录操作的结果，"0"：失败；"1"：成功
	 * @param operation
	 *            进行的操作
	 * @return
	 */
	private AjaxResult<String> result(String operator, Date start, String reason, String status, String operation) {
		Logger logger = ResultBuilder.operationLogger(operator, start, reason, status, operation);// 记录操作日志
		syLoggerService.save(logger);
		return ResultBuilder.byStatus(status, reason);
	}
}
