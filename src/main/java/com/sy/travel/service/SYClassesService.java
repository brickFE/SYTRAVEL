package com.sy.travel.service;

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
import com.sy.travel.entity.Classes;
import com.sy.travel.entity.Logger;
import com.sy.travel.utils.JSON;
/**
 * 分类信息的服务层处理
 * @author liuxin
 *
 */
@Service
public class SYClassesService implements DateFormat{
	@Autowired
	private SYProductService syProductService;
	@Autowired
	private SYClassesRepository syClassesRepository;
	@Autowired
	private SYLoggerService syLoggerService;
	
	/**
	 * 添加分类信息
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> add(JSON json, String operator) {
		Date start = new Date();
		operator = (String) json.get("operator");
		String reason = "";
		String name = (String) json.get("name");
		if(StringUtils.isBlank(name)) {
			reason = Commons.CLASSES_ADD_NAME_NOT_NULL;
			return result(operator, start, reason, "0", Commons.CLASSES_ADD);
		}
		Classes regexName = syClassesRepository.findByName(name);
		if(regexName != null) {
			reason = Commons.CLASSES_ADD_NAME_IS_EXISTS;
			return result(operator, start, reason, "0", Commons.CLASSES_ADD);
		}
		String regeSortId = (String) json.get("sortId");
		if(StringUtils.isBlank(regeSortId)) {
			reason = Commons.CLASSES_ADD_SORTID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.CLASSES_ADD);
		}
		String regex = "^[0-9]+$";
		if(!regeSortId.matches(regex)) {
			reason = Commons.CLASSES_ADD_SORTID_IS_EXISTS;
			return result(operator, start, reason, "0", Commons.CLASSES_ADD);
		}
		int sortId = Integer.valueOf(regeSortId);
		int parentId = 0;
		if(!"undefined".equals((String)json.get("parentId"))) {
			parentId = Integer.valueOf((String) json.get("parentId"));
		}
		String remark = StringUtils.isBlank((String)json.get("remark")) ? "" : (String)json.get("remark");
		Classes classes = new Classes(name, sortId, parentId, remark);
		classes.setCreatedDate(sdf.format(new Date()));
		classes.setCreatedUser(operator);
		classes.setModifiedDate("");
		classes.setModifiedUser("");
		Classes resl = syClassesRepository.save(classes);
		if(resl == null) {
			reason = Commons.CLASSES_ADD_SAVE_FAILED;
			return result(operator, start, reason, "0", Commons.CLASSES_ADD);
		}
		return result(operator, start, reason, "1", Commons.CLASSES_ADD);
	}
	
	/**
	 * 删除所选分类信息
	 * @param id
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> delete(int id, String operator) {
		Date start = new Date();
		String reason = "";
		Classes classes = syClassesRepository.findOne(id);
		if(classes == null) {
			reason = Commons.CLASSES_DELETE_DATA_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.CLASSES_DELETE);
		}
		//查看父类id为此分类id的所有子类信息
		List<Classes> list = syClassesRepository.findByParentId(id);
		if(list == null || list.size() == 0) {
			syClassesRepository.delete(id);
		} else {
			reason = Commons.CLASSES_DELETE_HAVE_CHILD;
			return result(operator, start, reason, "0", Commons.CLASSES_DELETE);
		}
		return result(operator, start, reason, "1", Commons.CLASSES_DELETE);
	}
	
	/**
	 * 更新分类信息
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> update(JSON json, String operator) {
		operator = (String) json.get("operator");
		Date start = new Date();
		String reason = "";
		int id = Integer.valueOf((String)json.get("id"));
		Classes classes = syClassesRepository.findOne(id);
		String name = (String) json.get("name");
		if(StringUtils.isBlank(name)) {
			name = classes.getName();
		} else {
			Classes regexName = syClassesRepository.findByName(name);
			if(regexName != null && regexName.getId() != id) {
				reason = Commons.CLASSES_ADD_NAME_IS_EXISTS;
				return result(operator, start, reason, "0", Commons.CLASSES_ADD);
			}
		}
		String regeSortId = (String) json.get("sortId");
		int sortId = 0;
		if(StringUtils.isBlank(regeSortId)) {
			sortId = classes.getSortId();
		} else {
			String regex = "^[0-9]+$";
			if(!regeSortId.matches(regex)) {
				reason = Commons.CLASSES_ADD_SORTID_IS_EXISTS;
				return result(operator, start, reason, "0", Commons.CLASSES_ADD);
			}
			sortId = Integer.valueOf(regeSortId);
		}
		int parentId = 0;
		if(StringUtils.isBlank((String)json.get("parentId"))) {
			parentId = classes.getParentId();
		} else {
			parentId = sortId = Integer.valueOf((String)json.get("parentId"));
		}
		String remark = StringUtils.isBlank((String)json.get("remark")) ? classes.getRemark() : (String)json.get("remark");
		Classes saveC = new Classes(name, sortId, parentId, remark);
		saveC.setCreatedDate(classes.getCreatedDate());
		saveC.setCreatedUser(classes.getCreatedUser());
		saveC.setModifiedDate(sdf.format(new Date()));
		saveC.setId(id);
		saveC.setModifiedUser(operator);
		Classes resl = syClassesRepository.saveAndFlush(saveC);
		if(resl == null) {
			reason = Commons.CLASSES_ADD_SAVE_FAILED;
			return result(operator, start, reason, "0", Commons.CLASSES_ADD);
		}
		return result(operator, start, reason, "1", Commons.CLASSES_UPDATE);
	}
	
	/**
	 * 查询分类信息：
	 * 当name为空时，查询的是所有的分类的信息
	 * 当name不为空时，根据name进行模糊查询
	 * @param name
	 * @return
	 */
	public Map<String, Object> queryAll(String name, int currentPage, int pageSize){
		Map<String, Object> resultMap = new HashMap<>();
		Page<Classes> list = null;
		Sort sort = new Sort(Direction.DESC, "sortId");
		Pageable page = new PageRequest(currentPage - 1, pageSize, sort);
		long count = 0;
		if(StringUtils.isBlank(name)) {
			list = syClassesRepository.findAll(page);
			count = syClassesRepository.count();
		} else {
			name = "%" + name + "%";
			list = syClassesRepository.findByNameLike(name, page);
			count = syClassesRepository.countByNameLike(name);
		}
		if(list == null || list.getSize() == 0) {
			resultMap.put("total", count);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> tempList = new ArrayList<>();
		for(Classes c : list) {
			Map<String, Object> temp = new HashMap<>(c.toMap());
			if(c.getParentId() == 0) {
				temp.put("parentName", "");
			} else {
				Classes tempC = syClassesRepository.findOne(c.getParentId());
				temp.put("parentName", tempC.getName());
			}
			tempList.add(temp);
		}
		resultMap.put("total", count);
		resultMap.put("documents", tempList);
		return resultMap;
	}
	
	/**
	 * 根据id查看这个分类下的所有产品信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> info(String id) {
		if(StringUtils.isBlank(id)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("documents", new ArrayList<>());
			return map;
		}
		return syProductService.findByClassId(id);
	}
	
	/**
	 * 对分类模块操作结果的统一处理
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
		Logger logger = new Logger(operator, sdf.format(start), sdf.format(new Date()),
				ResultBuilder.operationMessage(operator, operation, reason),
				status, operation);// 记录操作日志
		syLoggerService.save(logger);
		return ResultBuilder.byStatus(status, reason);
	}
}
