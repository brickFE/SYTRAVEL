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
import com.sy.travel.dao.SYProductRepository;
import com.sy.travel.dao.SYProjectRepository;
import com.sy.travel.dao.SYRoleRepository;
import com.sy.travel.dao.SYTeamRepository;
import com.sy.travel.dto.team.TeamCreateRequest;
import com.sy.travel.dto.team.TeamUpdateRequest;
import com.sy.travel.entity.Product;
import com.sy.travel.entity.Project;
import com.sy.travel.entity.Role;
import com.sy.travel.entity.Team;
import com.sy.travel.service.support.OperationResultSupport;
import com.sy.travel.utils.JSON;

/**
 * 团队管理业务逻辑处理服务层：
 * 
 * @author liuxin
 *
 */
@Service
public class SYTeamService implements DateFormat{
	@Autowired
	private SYTeamRepository teamRepository;
	@Autowired
	private SYProjectRepository projectRepository;
	@Autowired
	private SYProductRepository syProductRepository;
	@Autowired
	private SYProductService syProductService;
	@Autowired
	private SYLoggerService syLoggerService;
	@Autowired
	private SYRoleRepository syRoleRepository;

	/**
	 * 查询所有团队信息的逻辑操作
	 * 
	 * @param name
	 *            团队名字，当name为空时，查询所有的团队信息；不为空时根据名字进行模糊查询
	 * @return
	 */
	public Map<String, Object> findAll(String name, int currentPage, int pageSize) {
		Page<Team> teamList = null;// 保存查询到的团队信息的临时list
		Map<String, Object> resultMap = new HashMap<>();// 保存固定返回格式的map集
		/*
		 * if(name==""||name==null) {这种不会进入这个条件中 if("".equals(name) || name == null) {
		 */
		Sort sort = new Sort(Direction.DESC, "name");//按团队名倒序排序
		Pageable page = new PageRequest(currentPage - 1, pageSize, sort);
		long count = 0;//记录查到的总记录数
		if (StringUtils.isBlank(name)) {
			teamList = teamRepository.findAll(page);
			count = teamRepository.count();
		} else {
			name = "%" + name + "%";// 模糊查询
			teamList = teamRepository.findByNameLike(name, page);
			count = teamRepository.countByNameLike(name);
		}
		if (teamList == null || teamList.getSize() == 0) {
			resultMap.put("total", 0);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> temp = new ArrayList<>();
		for (Team team : teamList) {
			Map<String, Object> tempMap = new HashMap<>(team.toMap());
			List<Role> listRole = syRoleRepository.findByTeamId(team.getId());
			if(listRole==null || listRole.size()==0) {
				tempMap.put("roleNum", 0);
			} else {
				tempMap.put("roleNum", listRole.size());
			}
			tempMap.put("pname", projectRepository.findById(Integer.valueOf(team.getProjectId())).getName());
			temp.add(tempMap);
		}
		resultMap.put("total", count);
		resultMap.put("documents", temp);
		return resultMap;
	}

	/**
	 * 根据团队ID查找这个团队信息的逻辑操作
	 * 
	 * @param id
	 *            要查找的团的I
	 * @param operator
	 *            操作人
	 * @return
	 */
	public Team findOne(Integer id) {
		Team result = teamRepository.findOne(id);
		if (result == null) {
			return null;
		}
		return result;
	}

	/**
	 * 添加一个团队信息的逻辑操作
	 * 
	 * @param team
	 *            要添加的团队信息
	 * @return
	 */
	public AjaxResult<String> add(JSON json, String operator) {
		operator = (String) json.get("operator");
		Date start = new Date();
		String name = (String) json.get("name");
		String reason = "";
		if (StringUtils.isBlank(name)) {
			reason = Commons.TEAM_ADD_NAME_NOT_NULL;
			return result(operator, start, reason, "0", Commons.TEAM_ADD);
		}
		Team data = teamRepository.findByName(name);
		if (data != null) {
			reason = Commons.TEAM_ADD_NAME_EXISTS;
			return result(operator, start, reason, "0", Commons.TEAM_ADD);
		}
		String regex = "^[\\w\\u4e00-\\u9fa5]+$";
		if (!name.matches(regex)) {
			reason = Commons.TEAM_ADD_NAME_FORMATE;
			return result(operator, start, reason, "0", Commons.TEAM_ADD);
		}
		if (StringUtils.isBlank((String) json.get("projectId"))) {
			reason = Commons.TEAM_ADD_PROJECTID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.TEAM_ADD);
		}
		String projectId = (String) json.get("projectId");
		Project regexP = projectRepository.findOne(Integer.valueOf(projectId));
		if(regexP == null) {
			reason = Commons.PROJECT_UPDATE_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.TEAM_ADD);
		}
		String valid = (String) json.get("valid");
		if(!"0".equals(valid) && !"1".equals(valid) || "".equals(valid)) {
			reason = Commons.TEAM_ADD_VALID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.TEAM_ADD);
		}
		String remark = (String) json.get("remark");
		Team team = new Team(name, projectId, valid, StringUtils.isBlank(remark)?"":remark, operator);
		team.setCreatedDate(sdf.format(new Date()));
		team.setModifiedUser("");
		team.setModifiedDate("");
		Team result = teamRepository.save(team);
		if (result == null) {
			reason = Commons.TEAM_ADD_SAVE_FAILED;
			return result(operator, start, reason, "0", Commons.TEAM_ADD);
		}
		return result(operator, start, reason, "1", Commons.TEAM_ADD);
	}

	public AjaxResult<String> add(TeamCreateRequest request) {
		JSON json = new JSON();
		json.put("operator", request.getOperator());
		json.put("name", request.getName());
		json.put("projectId", request.getProjectId());
		json.put("valid", request.getValid());
		json.put("remark", request.getRemark());
		return add(json, request.getOperator());
	}

	public Map<String, Object> queryByProjectId(String projectId) {
		Map<String, Object> resultMap = new HashMap<>();
		if (StringUtils.isBlank(projectId)) {
			resultMap.put("total", 0);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Team> teamList = teamRepository.findByProjectId(projectId);
		if (teamList == null || teamList.size() == 0) {
			resultMap.put("total", 0);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> temp = new ArrayList<>();
		for (Team team : teamList) {
			Map<String, Object> tempMap = new HashMap<>(team.toMap());
			tempMap.put("pname", projectRepository.findById(Integer.valueOf(team.getProjectId())).getName());
			temp.add(tempMap);
		}
		resultMap.put("total", teamList.size());
		resultMap.put("documents", temp);
		return resultMap;
	}

	/**
	 * 通过团队id删除这个团队的信息 1：根据团队id删除这个团队如果id为空则删除失败； 2：根据这个团队的id查看这个团队下的所有产品信息，然后全部删除
	 * 
	 * @param id
	 *            团队id
	 * @param operator
	 *            操作者
	 * @return
	 */
	public AjaxResult<String> deleteByTeamId(Integer id, String operator) {
		Date start = new Date();
		String reason = "";
		try {
			teamRepository.delete(id);
			//删除这个团队下的产品信息
			List<Product> productList = syProductRepository.findByTeamId(id);
			if (productList != null && productList.size() != 0) {
				for (Product p : productList) {
					syProductRepository.delete(p.getId());
				}
			}
			//删除这个团队下的角色信息
			List<Role> roleList = syRoleRepository.findByTeamId(id);
			if(roleList != null && roleList.size() != 0) {
				for(Role r : roleList) {
					syRoleRepository.delete(r.getId());
				}
			}
			return result(operator, start, reason, "1", Commons.TEAM_DELETE);
		} catch (Exception e) {
			reason = Commons.TEAM_DELETE_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.TEAM_DELETE);
		}
	}
	
	/**
	 * 当项目状态为禁用时，或者要修改团队状态为禁用时，把所有的团队及团队下的产品状态都修改为禁用
	 * @param id
	 */
	public void updateValidById(int id){
		Team team = teamRepository.findOne(id);
		if(team != null) {
			team.setValid("0");
			Team temp = teamRepository.save(team);
			if(temp == null) {
				throw new RuntimeException("状态更新失败");
			}
			List<Product> pList = syProductRepository.findByTeamId(id);
			//查看这个团队下产品信息，如果有就下架产品
			if(pList != null) {
				for(Product p : pList) {
					p.setStatus("2");
				}
			}
		}
	}
	
	/**
	 * 修改团队信息
	 */
	public AjaxResult<String> update(JSON json, String operator) {
		operator = (String) json.get("operator");
		Date start = new Date();
		String reason = "";
		if (StringUtils.isBlank((String) json.get("id"))) {
			reason = Commons.PROJECT_UPDATE_ID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.TEAM_UPDATE);
		}
		int id = Integer.valueOf((String) json.get("id"));
		Team teamById = teamRepository.findOne(id);
		if (teamById == null) {
			reason = Commons.TEAM_DELETE_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.TEAM_UPDATE);
		}
		String name = (String) json.get("name");
		if(StringUtils.isBlank(name)) {
			name = teamById.getName();
		} else {
			Team data = teamRepository.findByName(name);
			if (data != null && data.getId() != id) {
				reason = Commons.TEAM_ADD_NAME_EXISTS;
				return result(operator, start, reason, "0", Commons.TEAM_UPDATE);
			}
			String regex = "^[\\w\\u4e00-\\u9fa5]+$";
			if (!name.matches(regex)) {
				reason = Commons.TEAM_ADD_NAME_FORMATE;
				return result(operator, start, reason, "0", Commons.TEAM_UPDATE);
			}
		}
		String projectId = (String) json.get("projectId");
		if (StringUtils.isBlank(projectId)) {
			reason = Commons.TEAM_ADD_PROJECTID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.TEAM_UPDATE);
		} else {
			Project regexP = projectRepository.findOne(Integer.valueOf(projectId));
			if(regexP == null) {
				reason = Commons.PROJECT_UPDATE_NOT_EXISTS;
				return result(operator, start, reason, "0", Commons.TEAM_ADD);
			}
		}
		String valid = (String) json.get("valid");
		if (StringUtils.isBlank(valid)) {
			reason = Commons.TEAM_ADD_VALID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.TEAM_UPDATE);
		}
		String remark = StringUtils.isBlank((String) json.get("remark"))?"":(String) json.get("remark");
		teamById.setProjectId(projectId);
		teamById.setValid(valid);
		teamById.setModifiedUser(operator);
		teamById.setModifiedDate(sdf.format(new Date()));
		teamById.setName(name);
		teamById.setRemark(remark);
		teamRepository.saveAndFlush(teamById);
		if("0".equals(valid)) {
			updateValidById(id);
		}
		return result(operator, start, reason, "1", Commons.TEAM_UPDATE);
	}

	public AjaxResult<String> update(TeamUpdateRequest request) {
		JSON json = new JSON();
		json.put("operator", request.getOperator());
		json.put("id", request.getId() == null ? null : String.valueOf(request.getId()));
		json.put("name", request.getName());
		json.put("projectId", request.getProjectId());
		json.put("valid", request.getValid());
		json.put("remark", request.getRemark());
		return update(json, request.getOperator());
	}
	
	/**
	 * 根据团队id查看这个团队下所有的产品信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AjaxResult<List<Map<String, Object>>> info(int id) {
		Map<String, Object> temp = syProductService.findByTeamId(String.valueOf(id));
		int totalP = (int) temp.get("total");
		if (totalP == 0) {
			return AjaxResult.success(new ArrayList<>());
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) temp.get("documents");
		return AjaxResult.success(list);
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
