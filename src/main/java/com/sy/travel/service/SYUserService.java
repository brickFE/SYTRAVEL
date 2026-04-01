package com.sy.travel.service;

import java.util.ArrayList;
import java.util.Base64;
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
import com.sy.travel.dao.SYUserRepository;
import com.sy.travel.dto.user.UserAdminPwdUpdateRequest;
import com.sy.travel.dto.user.UserCreateRequest;
import com.sy.travel.dto.user.UserUpdateRequest;
import com.sy.travel.entity.Logger;
import com.sy.travel.entity.User;


/**
 * 用户的服务层处理
 * @author liuxin
 *
 */
@Service
public class SYUserService implements DateFormat{
	@Autowired
	private SYUserRepository syUserRepository;
	@Autowired
	private SYLoggerService syLoggerService;
	private String setPassword(String username, String password) {
		String pwd = username + ":" + password;
		byte[] encode = Base64.getEncoder().encode(pwd.getBytes());
		return new String(encode);
	}
	/**
	 * 添加用户信息
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> add(UserCreateRequest request){
		String operator = request.getOperator();
		Date start = new Date();
		String reason = "";
		String username = request.getUsername();
		if(StringUtils.isBlank(username)) {
			reason = Commons.USER_ADD_USRENAME_NOT_NULL;
			return result(operator, start, reason, "0", Commons.USER_ADD);
		}
		User regexName = syUserRepository.findByUsername(username);
		if(regexName != null) {
			reason = Commons.USER_ADD_USRENAME_IS_EXISTS;
			return result(operator, start, reason, "0", Commons.USER_ADD);
		}
		String password = request.getPassword();
		if(StringUtils.isBlank(password)) {
			reason = Commons.USER_ADD_PASSWORD_NOT_NULL;
			return result(operator, start, reason, "0", Commons.USER_ADD);
		}
		String permissionStr = request.getPermission();
		if(StringUtils.isBlank(permissionStr)) {
			reason = Commons.USER_ADD_PERMISSION_NOT_NULL;
			return result(operator, start, reason, "0", Commons.USER_ADD);
		}
		String remark = request.getRemark();
		remark = StringUtils.isBlank(remark)? "" : remark;
		User user = new User(username, setPassword(username, password), remark, permissionStr);
		User resl = syUserRepository.save(user);
		if(resl == null) {
			reason = Commons.USER_ADD_SAVE_FAILED;
			return result(operator, start, reason, "0", Commons.ROLE_ADD);
		}
		return result(operator, start, reason, "1", Commons.ROLE_ADD);
	}
	
	/**
	 * 删除用户信息
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> delete(int id, String operator){
		Date start = new Date();
		String reason = "";
		try {
			syUserRepository.delete(id);
			return result(operator, start, reason, "1", Commons.USER_DELETE);
		} catch (Exception e) {
			reason = Commons.USER_DELETE_FAILED;
			return result(operator, start, reason, "0", Commons.USER_DELETE);
		}
	}
	
	/**
	 * 通过用户名获得用户的权限
	 * @param username
	 * @return
	 */
	public AjaxResult<String> getPermission(String username){
		User user = syUserRepository.findByUsername(username);
		if(user == null) {
			return AjaxResult.failed(400, Commons.USER_NOT_LOGIN);
		} else {
			return AjaxResult.success(user.getPermission());
		}
	}
	
	/**
	 * 修改用户信息
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> update(UserUpdateRequest request){
		String operator = request.getOperator();
		Date start = new Date();
		String reason = "";
		Integer id = request.getId();
		if(id == null) {
			reason = Commons.USER_UPDATE_NOT_FOUND;
			return result(operator, start, reason, "0", Commons.USER_UPDATE);
		}
		User uTemp = syUserRepository.findOne(id);
		if(uTemp == null) {
			reason = Commons.USER_UPDATE_NOT_FOUND;
			return result(operator, start, reason, "0", Commons.USER_UPDATE);
		}
		String password = request.getPassword();
		if(StringUtils.isBlank(password)) {
			password = uTemp.getPassword();
		} 
		String permissionStr = request.getPermission();
		if(StringUtils.isBlank(permissionStr)) {
			permissionStr = uTemp.getPermission();
		}
		String remark = request.getRemark();
		if(StringUtils.isBlank(remark)) {
			remark = uTemp.getRemark();
		}
		User user = new User(uTemp.getUsername(), setPassword(uTemp.getUsername(), password), remark, permissionStr);
		user.setId(id);
		User resl = syUserRepository.save(user);
		if(resl == null) {
			reason = Commons.USER_ADD_SAVE_FAILED;
			return result(operator, start, reason, "0", Commons.USER_UPDATE);
		}
		return result(operator, start, reason, "1", Commons.USER_UPDATE);
	}
	
	/**
	 * 修改超级管理员密码
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> updateAdminPwd(UserAdminPwdUpdateRequest request){
		String operator = request.getOperator();
		Date start = new Date();
		String reason = "";
		if(!"admin".equals(operator)) {
			reason = Commons.USER_UPDATE_PWD_NOT_PERMISSION;
			return result(operator, start, reason, "0", Commons.USER_UPDATE_PWD);
		}
		User uTemp = syUserRepository.findByUsername("admin");
		if(uTemp == null) {
			reason = Commons.USER_UPDATE_NOT_FOUND;
			return result(operator, start, reason, "0", Commons.USER_UPDATE_PWD);
		}
		String password = request.getPassword();
		if(StringUtils.isBlank(password)) {
			reason = Commons.USER_ADMIN_OLD_PASSWORD_EMPTY;
			return result(operator, start, reason, "0", Commons.USER_UPDATE_PWD);
		} 
		if(!password.equals(uTemp.getPassword())) {
			reason = Commons.USER_ADMIN_OLD_PASSWORD_ERROR;
			return result(operator, start, reason, "0", Commons.USER_UPDATE_PWD);
		}
		String newPwd = request.getNewPwd();
		if(StringUtils.isBlank(newPwd)) {
			reason = Commons.USER_ADMIN_NEW_PASSWORD_EMPTY;
			return result(operator, start, reason, "0", Commons.USER_UPDATE_PWD);
		} 
		User user = new User(uTemp.getUsername(), setPassword(uTemp.getUsername(), newPwd), uTemp.getRemark(), uTemp.getPermission());
		user.setId(uTemp.getId());
		User resl = syUserRepository.save(user);
		if(resl == null) {
			reason = Commons.USER_ADD_SAVE_FAILED;
			return result(operator, start, reason, "0", Commons.USER_UPDATE_PWD);
		}
		return result(operator, start, reason, "1", Commons.USER_UPDATE_PWD);
	}
	

	/**
	 * 查看所有的团队信息
	 * 
	 * @param teamId
	 * @return
	 */
	public Map<String, Object> queryAll(String username, int currentPage, int pageSize) {
		Sort sort = new Sort(Direction.DESC, "permission");
		Pageable page = new PageRequest(currentPage-1, pageSize, sort);
		long count = 0;
		Map<String, Object> resultMap = new HashMap<>();
		Page<User> list = null;
		if (StringUtils.isBlank(username)) {
			list = syUserRepository.findAll(page);
			count = syUserRepository.count();
		} else {
			username = "%" + username + "%";
			list = syUserRepository.findByUsernameLike(username, page);
			count = syUserRepository.countByUsernameLike(username);
		}
		if (list == null || list.getSize() == 0) {
			resultMap.put("total", count);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> tempList = new ArrayList<>();
		for (User user : list) {
			if("admin".equals(user.getUsername())) {
				continue;
			}
			Map<String, Object> tempMap = new HashMap<>(user.toMap());
			tempMap.remove("password");
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
		Logger logger = new Logger(operator, sdf.format(start), sdf.format(new Date()),
				StringUtils.isBlank(reason) ? operator + operation + ":成功" : operator + operation + "失败原因:" + reason,
				status, operation);// 记录操作日志
		syLoggerService.save(logger);
		return ResultBuilder.byStatus(status, reason);
	}
}
