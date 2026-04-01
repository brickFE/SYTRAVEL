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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.common.ResultBuilder;
import com.sy.travel.common.Commons;
import com.sy.travel.common.DateFormat;
import com.sy.travel.dao.SYProjectRepository;
import com.sy.travel.entity.Logger;
import com.sy.travel.entity.Project;
import com.sy.travel.utils.JSON;


/**
 * 项目模块的服务层：
 * f
 * @author liuxin
 *
 */
@Service
public class SYProjectService implements DateFormat{
	@Autowired
	private SYProjectRepository projectRepository;
	@Autowired
	private SYTeamService syTeamService;
	@Autowired
	private SYProductService syProductService;
	@Autowired
	private SYLoggerService syLoggerService;

	/**
	 * 添加项目信息
	 * 
	 * @param json
	 *            要添加的项目的信息
	 * @param operator
	 *            操作者
	 * @return
	 */
	public AjaxResult<String> add(JSON json, String operator) {
		operator = (String) json.get("operator");// 操作人
		Date start = new Date();// 添加操作开始时间
		String reason = "";// 记录操作过程中的操作结果
		String code = (String) json.get("code");
		if (StringUtils.isBlank(code)) {
			reason = Commons.PROJECT_ADD_CODE_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		// 判断编号是否重复
		Project regexCode = projectRepository.findByCode(code);
		if (regexCode != null) {
			reason = Commons.PROJECT_ADD_CODE_IS_EXISTS;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		// 对项目编号的正则表达式
		/*
		 * String regex = "^[S][Y][\-][0-9] {8}[\-][A-Z] {2}[\-][A-Z] {2}[0-9] {3}&"; if
		 * (!code.matches(regex)) { return new AjaxResult<String>(200, "failed",
		 * "项目编号格式错误，格式例：SY-20180124-CN-BJ-001"); }
		 */
		String name = (String) json.get("name");
		if (StringUtils.isBlank(name)) {
			reason = Commons.PROJECT_ADD_NAME_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		// 需要判断名称是否重复
		Project regexName = projectRepository.findByName(name);
		if (regexName != null) {
			reason = Commons.PROJECT_ADD_NAME_IS_EXISTS;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		// 项目 开始时间
		String beginDate = (String)json.get("beginDate");
		if(StringUtils.isBlank(beginDate)) {
			reason = Commons.PROJECT_ADD_BEGINDATE_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		Date beDate = new Date();
		try {
			beDate = sdf.parse((String)json.get("beginDate")+".000");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 项目结束时间
		String endDate = (String)json.get("endDate");
		if(StringUtils.isBlank(endDate)) {
			reason = Commons.PROJECT_ADD_ENDDATE_NOT_NUlL;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		Date eDate = new Date();
		try {
			eDate = sdf.parse((String)json.get("endDate")+".000");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 比较项目开始时间与结束时间：如果结束时间在开始时间之前，则添加失败
		if (eDate.before(beDate)) {
			reason = Commons.PROJECT_ADD_DATE_NOT_BEFORE;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		String valid = (String) json.get("valid");
		if (StringUtils.isBlank(valid)) {
			reason = Commons.PROJECT_ADD_VALID;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		String remark = (String) json.get("remark");// 备注信息
		remark = StringUtils.isBlank(remark) ? "" : remark;
		Project project = new Project(code, name, beginDate, endDate, valid, remark, operator, sdf.format(new Date()));
		project.setModifiedUser("");
		project.setModifiedDate("");
		Project flag = projectRepository.save(project);// 保存成功则返回这个对象
		if (flag == null) {
			reason = Commons.PROJECT_ADD_SAVE_FALIED;// 500
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		// 当成功添加项目后，reason为""
		return result(operator, start, reason, "1", Commons.PROJECT_ADD);
	}

	/**
	 * 根据id删除这个项目及项目下的所有团队和产品信息
	 * 
	 * @param id
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> deleteById(int id, String operator) {
		Date start = new Date();
		String reason = "";
		try {
			// 根据项目id查询这个项目下的团队信息
			Map<String, Object> teamMap = syTeamService.queryByProjectId(String.valueOf(id));
			int total = (int) teamMap.get("total");
			// 如果有团队信息就删除
			if (total != 0) {
				List<Map<String, Object>> teamList = (List<Map<String, Object>>) teamMap.get("documents");
				for (Map<String, Object> team : teamList) {
					int teamId = (int) team.get("id");
					syTeamService.deleteByTeamId(teamId, operator);
				}
			}
			// 删除项目信息
			projectRepository.delete(id);
			return result(operator, start, reason, "1", Commons.PROJECT_DELETE);
		} catch (Exception e) {
			reason = Commons.PROJECT_DELETE_ID_NOT_OR_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.PROJECT_DELETE);
		}
	}

	/**
	 * 更新项目信息
	 * 
	 * @param json
	 * @param operator
	 * @return
	 */
	public AjaxResult<String> update(JSON json, String operator) {
		operator = (String) json.get("operator");
		Date start = new Date();
		String reason = "";
		if (StringUtils.isBlank((String) json.get("id"))) {
			reason = Commons.PROJECT_UPDATE_ID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PROJECT_UPDATE);
		}
		int id = Integer.valueOf((String) json.get("id"));
		Project project = null;
		try {
			project = projectRepository.findOne(id);
		} catch (Exception e) {
			reason = Commons.PROJECT_UPDATE_ID_NOT_NULL;
			return result(operator, start, reason, "0", Commons.PROJECT_UPDATE);
		}
		if (project == null) {
			reason = Commons.PROJECT_UPDATE_NOT_EXISTS;
			return result(operator, start, reason, "0", Commons.PROJECT_UPDATE);
		}
		String code = (String) json.get("code");
		if (StringUtils.isBlank(code)) {
			code = project.getCode();
		} else {
			Project regexCode = projectRepository.findByCode(code);
			if (regexCode != null && regexCode.getId() != id) {
				reason = Commons.PROJECT_ADD_CODE_IS_EXISTS;
				return result(operator, start, reason, "0", Commons.PROJECT_ADD);
			}
		}
		String name = (String) json.get("name");
		if (StringUtils.isBlank(name)) {
			name = project.getName();
		} else {
			// 需要判断名称是否重复
			Project regexName = projectRepository.findByName(name);
			if (regexName != null && regexName.getId() != id) {
				reason = Commons.PROJECT_ADD_NAME_IS_EXISTS;
				return result(operator, start, reason, "0", Commons.PROJECT_ADD);
			}
		}
		String beginDate = (String) json.get("beginDate");
		Date beDate = new Date();
		// 项目 开始时间
		if(StringUtils.isBlank(beginDate)) {
			beginDate = project.getBeginDate();
		} else {
			try {
				beDate = sdf.parse(beginDate+".000");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 项目结束时间
		String endDate = (String) json.get("endDate");
		Date eDate = new Date();
		if (StringUtils.isBlank(endDate)) {
			endDate = project.getEndDate();
		} else {
			try {
				eDate = sdf.parse(endDate+".000");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 比较项目开始时间与结束时间：如果结束时间在开始时间之前，则添加失败
		if (eDate.before(beDate)) {
			reason = Commons.PROJECT_ADD_DATE_NOT_BEFORE;
			return result(operator, start, reason, "0", Commons.PROJECT_ADD);
		}
		String remark = (String) json.get("remark");
		remark = StringUtils.isBlank(remark) ? "" : remark;
		String valid = (String) json.get("valid");
		if (StringUtils.isBlank(valid)) {
			valid = project.getValid();
		}
		/**
		 * 如果项目的状态修改为禁用，则这个项目下的所有团队及产品都是禁用的
		 */
		if ("0".equals(valid)) {
			Map<String, Object> teamMap = syTeamService.queryByProjectId(String.valueOf(id));
			int total = (int) teamMap.get("total");
			// 如果这个项目下没有信息，则不需要更新状态，如果有信息就需要更新
			if (total != 0) {
				List<Map<String, Object>> teamList = (List<Map<String, Object>>) teamMap.get("documents");
				for (Map<String, Object> team : teamList) {
					int teamId = (int) team.get("id");
					syTeamService.updateValidById(teamId);
				}
			}
		}
		/**
		 * 1、调用保存实体的方法
		 * 
		 * 1）保存一个实体：repository.save(T entity)
		 * 
		 * 2）保存多个实体：repository.save(Iterable<T> entities)
		 * 
		 * 3）保存并立即刷新一个实体：repository.saveAndFlush(T entity)
		 * 注：若是更改，entity中必须设置了主键字段，不然不能对应上数据库中的记录，变成新增（数据库自动生成主键）或报错（数据库不自动生成主键）了
		 */
		Project updateProject = new Project(code, name, beginDate, endDate, valid, remark, project.getCreatedUser(),
				project.getCreatedDate());
		updateProject.setId(id);
		//需要把主键id传入才能修改，少了id就不能修改这个id所代表的项目信息，变为添加了
		updateProject.setModifiedDate(sdf.format(new Date()));
		updateProject.setModifiedUser(operator);
		Project reslProject = projectRepository.save(updateProject);
		if (reslProject == null) {
			reason = Commons.PROJECT_UPDATE_NOT_DATABASE;
			return result(operator, start, reason, "0", Commons.PROJECT_UPDATE);
		}
		return result(operator, start, reason, "1", Commons.PROJECT_UPDATE);
	}

	/**
	 * 查看所有项目的信息 根据项目名称查询这个项目的信息 当项目名称为空时，返回的是所有项目的信息
	 * 
	 * @param projectName
	 *            项目名称
	 * @return
	 */
	public Map<String, Object> queryAll(String projctName, int currentPage, int pageSize) {
		// 保存的是从数据库查询到的结果
		//按项目开始时间升序排列
		Sort sort = new Sort(Direction.ASC, "beginDate");
		PageRequest page = new PageRequest(currentPage-1, pageSize, sort);
		Page<Project> pageList = null;
		long count = 0 ;
		// 传入的项目名称是否为空，为空则查询所有项目的信息，不为空则查询根据传入的项目名称查询这个项目的信息
		if (StringUtils.isBlank(projctName)) {
			pageList = projectRepository.findAll(page);
			count = projectRepository.count();
		} else {
			count = projectRepository.countByNameLike("%" + projctName + "%");
			pageList = projectRepository.findByNameLike("%" + projctName + "%", page);
		}
		Map<String, Object> resultMap = new HashMap<>();// 用来封装查询后要返回的数据
		if(pageList == null || pageList.getSize() == 0) {
			resultMap.put("total", count);
			resultMap.put("documents", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> tempList = new ArrayList<>();
		for (Project project : pageList) {
			Map<String, Object> temp = new HashMap<>(project.toMap());
			tempList.add(temp);
		}
		resultMap.put("total", count);
		resultMap.put("documents", tempList);
		return resultMap;
	}

	/**
	 * 根据id查看这个项目下的所有团队信息以及所有产品信息
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> info(int id) {
		Map<String, Object> resultMap = new HashMap<>();// 用来封装查询后要返回的数据
		if (id == 0) {
			resultMap.put("team", new ArrayList<>());
			resultMap.put("product", new ArrayList<>());
			return resultMap;
		}
		// 根据项目id查看这个项目下的所有团队信息
		Map<String, Object> teamMap = syTeamService.queryByProjectId(String.valueOf(id));
		int total = (int) teamMap.get("total");
		if (total == 0) {
			resultMap.put("team", new ArrayList<>());
			resultMap.put("product", new ArrayList<>());
			return resultMap;
		}
		List<Map<String, Object>> teamList = (List<Map<String, Object>>) teamMap.get("documents");
		resultMap.put("team", teamList);
		List<Map<String, Object>> productList = new ArrayList<>();
		for (Map<String, Object> team : teamList) {
			int teamId = (int) team.get("id");
			// 根据团队id查看这个团队下所有产品的信息
			Map<String, Object> temp = syProductService.findByTeamId(String.valueOf(teamId));
			int totalP = (int) temp.get("total");
			if (totalP != 0) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) temp.get("documents");
				productList = list;
			}
		}
		if(productList == null || productList.size() == 0) {
			resultMap.put("product", new ArrayList<>());
		} else {
			resultMap.put("product", productList);
		}
		return resultMap;
	}
	
	/**
	 * 对项目操作结果的统一处理
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
