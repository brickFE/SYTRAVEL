package com.sy.travel.rest;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.dto.team.TeamCreateRequest;
import com.sy.travel.dto.team.TeamUpdateRequest;
import com.sy.travel.service.SYTeamService;

/**
 * 团队管理接口： 查看所有团队信息、
 * 
 * @author liuxin
 *
 */
@RestController
@Validated
@RequestMapping(value = "/sy/team", produces = MediaType.APPLICATION_JSON_VALUE)
public class SYTeamRest {
	@Autowired
	private SYTeamService syTeamService;

	/**
	 * 查看所有团队信息的接口 consumes="application/json" get模式不能加这些,post需要加
	 * 
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> findAll(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "1") @Min(1) int currentPage,
			@RequestParam(defaultValue = "5") @Min(1) int pageSize) {
		return AjaxResult.success(syTeamService.findAll(name,currentPage,pageSize));
	}

	/**
	 * 1.页面以json串的格式传入要添加的团队信息 2.json串转换为java对象 3.利用jpa的api语法实现团队信息的添加
	 * 
	 * @param name
	 * @param age
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> add(@Valid @RequestBody TeamCreateRequest request) {
		return syTeamService.add(request);
	}

	/**
	 * 根据id查看这个团队下的所有产品信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public AjaxResult<List<Map<String, Object>>> info(@RequestParam("id") @Min(1) int id){
		return syTeamService.info(id);
	}
	
	/**
	 * 通过项目id查看此项目下的团队信息
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/allbypid", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> findAllByProjectId(@RequestParam(defaultValue = "") String projectId) {
		Map<String, Object> data = syTeamService.queryByProjectId(projectId);
		return AjaxResult.success(data);
	}

	/**
	 * 删除所选团队信息的接口
	 * 
	 * @param id
	 *            所选团队id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> deleteByTeamId(@RequestParam("id") @Min(1) Integer id, @RequestParam("operator") @NotBlank String operator) {
		return syTeamService.deleteByTeamId(id, operator);
	}
	/**
	 * 更新团队信息
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> update(@Valid @RequestBody TeamUpdateRequest request) {
		return syTeamService.update(request);
	}
}
