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
import com.sy.travel.common.Commons;
import com.sy.travel.common.DateFormat;
import com.sy.travel.dao.SYRoleRepository;
import com.sy.travel.dto.role.RoleCreateRequest;
import com.sy.travel.dto.role.RoleUpdateRequest;
import com.sy.travel.entity.Role;
import com.sy.travel.service.support.OperationResultSupport;
import com.sy.travel.utils.JSON;

/**
 * 角色的服务层处理
 * 
 * @author liuxin
 *
 */
@Service
public class SYRoleService implements DateFormat{
	@Autowired
	private SYRoleRepository syRoleRepository;
	@Autowired
	private SYTeamService syTeamService;
	@Autowired
	private SYLoggerService syLoggerService;

	/**
	 * 添加角色信息
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> add(JSON json, String operator){
		operator = (String) json.get("operator");
		Date start = new Date();
		String reason = "";
		String name = (String) json.get("name");
		if(StringUtils.isBlank(name)) {
			reason = Commons.ROLE_ADD_NAME_NOT_NULL;
			return result(operator, start, reason, "0", Commons.ROLE_ADD);
		}
		Role regexName = syRoleRepository.findByName(name);
		if(regexName != null) {
			reason = Commons.ROLE_ADD_NAME_IS_EXISTS;
			return result(operator, start, reason, "0", Commons.ROLE_ADD);
		}
		if(StringUtils.isBlank((String) json.get("teamId"))){
			reason = Commons.ROLE_ADD_TEAMID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.ROLE_ADD);
		}
		int teamId = Integer.valueOf((String) json.get("teamId"));
		String role = (String) json.get("role");
		if(StringUtils.isBlank(role)) {
			reason = Commons.ROLE_ADD_ROLE_NOT_NULL;
			return result(operator, start, reason, "0", Commons.ROLE_ADD);
		}
		//邮箱可以不填
		String email = "";
		if(!StringUtils.isBlank((String) json.get("email"))) {
			email = (String) json.get("email");
			String regexEmail = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			if(!email.matches(regexEmail)) {
				reason = Commons.ROLE_ADD_EMAIL_ERROR;
				return result(operator, start, reason, "0", Commons.ROLE_ADD);
			}
		}
		String mobile = (String) json.get("mobile");
		//手机号必须填写
		if(StringUtils.isBlank(mobile)) {
			reason = Commons.ROLE_ADD_MOBILE_NOT_NULL;
			return result(operator, start, reason, "0", Commons.ROLE_ADD);
		}
		String regexMobile = "^1[3|4|5|7|8][0-9]{9}$";
		if(!mobile.matches(regexMobile)) {
			reason = Commons.ROLE_ADD_MOBILE_ERROR;
			return result(operator, start, reason, "0", Commons.ROLE_ADD);
		}
		String remark = (String) json.get("remark");
		remark = StringUtils.isBlank(remark)? "" : remark;
		Role roles = new Role(remark, name, role, teamId, operator, sdf.format(new Date()), email, mobile);
		roles.setModifiedDate("");
		roles.setModifiedUser("");
		Role resl = syRoleRepository.save(roles);
		if(resl == null) {
			reason = Commons.ROLE_ADD_SAVE_FAILED;
			return result(operator, start, reason, "0", Commons.ROLE_ADD);
		}
		return result(operator, start, reason, "1", Commons.ROLE_ADD);
	}

	public AjaxResult<String> add(RoleCreateRequest request) {
		JSON json = new JSON();
		json.put("operator", request.getOperator());
		json.put("name", request.getName());
		json.put("teamId", request.getTeamId());
		json.put("role", request.getRole());
		json.put("email", request.getEmail());
		json.put("mobile", request.getMobile());
		json.put("remark", request.getRemark());
		return add(json, request.getOperator());
	}
	
	/**
	 * 删除角色信息
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> delete(int id, String operator){
		Date start = new Date();
		String reason = "";
		try {
			syRoleRepository.delete(id);
			return result(operator, start, reason, "1", Commons.ROLE_DELETE);
		} catch (Exception e) {
			reason = Commons.ROLE_DELETE_FAILED;
			return result(operator, start, reason, "0", Commons.ROLE_DELETE);
		}
	}
	
	/**
	 * 修改角色信息
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> update(JSON json, String operator){
		operator = (String) json.get("operator");
		Date start = new Date();
		String reason = "";
		if(StringUtils.isBlank((String)json.get("id"))) {
			reason = Commons.ROLE_ADD_TEAMID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.ROLE_UPDATE);
		}
		int id = Integer.valueOf((String) json.get("id"));
		Role queryRole = syRoleRepository.findOne(id);
		if(queryRole == null) {
			reason = Commons.ROLE_UPDATE_IS_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.ROLE_UPDATE);
		}
		String role = (String) json.get("role");
		if(StringUtils.isBlank(role)) {
			role = queryRole.getRole();
		}
		String email = (String) json.get("email");
		if(StringUtils.isBlank(email)) {
			email = queryRole.getEmail();
		} else {
			String regexEmail = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			if(!email.matches(regexEmail)) {
				reason = Commons.ROLE_ADD_EMAIL_ERROR;
				return result(operator, start, reason, "0", Commons.ROLE_UPDATE);
			}
		}
		String mobile = (String) json.get("mobile");
		if(StringUtils.isBlank(mobile)) {
			mobile = queryRole.getMobile();
		} else {
			String regexMobile = "^1[3|4|5|7|8][0-9]{9}$";
			if(!mobile.matches(regexMobile)) {
				reason = Commons.ROLE_ADD_MOBILE_ERROR;
				return result(operator, start, reason, "0", Commons.ROLE_UPDATE);
			}
		}
		String remark = StringUtils.isBlank((String) json.get("remark"))? "" : (String) json.get("remark");
		Role roles = new Role(remark, queryRole.getName(), role, queryRole.getTeamId(), queryRole.getCreatedUser(), queryRole.getCreatedDate(), email, mobile);
		roles.setModifiedDate(sdf.format(new Date()));
		roles.setModifiedUser(operator);
		roles.setId(id);
		Role resl = syRoleRepository.saveAndFlush(roles);
		if(resl == null) {
			reason = Commons.ROLE_ADD_SAVE_FAILED;
			return result(operator, start, reason, "0", Commons.ROLE_UPDATE);
		}
		return result(operator, start, reason, "1", Commons.ROLE_UPDATE);
	}

	public AjaxResult<String> update(RoleUpdateRequest request){
		JSON json = new JSON();
		json.put("operator", request.getOperator());
		json.put("id", String.valueOf(request.getId()));
		json.put("role", request.getRole());
		json.put("email", request.getEmail());
		json.put("mobile", request.getMobile());
		json.put("remark", request.getRemark());
		return update(json, request.getOperator());
	}
	

	/**
	 * 查看所有的角色信息
	 * 
	 * @param teamId
	 * @return
	 */
	public Map<String, Object> queryAll(String teamId, int currentPage, int pageSize) {
		Map<String, Object> resultMap = new HashMap<>();
		Sort sort = new Sort(Direction.DESC, "teamId");
		Pageable page = new PageRequest(currentPage-1, pageSize, sort);
		long count = 0;
		Page<Role> list = null;
		if (StringUtils.isBlank(teamId)) {
			list = syRoleRepository.findAll(page);
			count = syRoleRepository.count();
		} else {
			page = new PageRequest(1, Integer.MAX_VALUE, sort);
			list = syRoleRepository.findByTeamId(Integer.valueOf(teamId), page);
			count = syRoleRepository.countByTeamId(Integer.valueOf(teamId));
		}
		if (list == null || list.getSize() == 0) {
			resultMap.put("total", count);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> tempList = new ArrayList<>();
		for (Role role : list) {
			Map<String, Object> tempMap = new HashMap<>(role.toMap());
			tempMap.put("tname", syTeamService.findOne(role.getTeamId()).getName());
			tempList.add(tempMap);
		}
		resultMap.put("total", count);
		resultMap.put("documents", tempList);
		return resultMap;
	}
	
	/**
	 * 对操作过程中的操作结果的统一处理
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
		return OperationResultSupport.build(syLoggerService, operator, start, reason, status, operation);
	}
}
