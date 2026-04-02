package com.sy.travel.common;
/**
 * 业务管理系统的常量池，所有的中文提示语全提炼出来，形成常量，减少错误
 * @author liuxin
 *
 */
public class Commons {
	/**
	 * 用户登录验证模块的常量
	 */
	public final static String LOGIN_CHECK_PWD_NOT_NULl = "密码不能为空";
	public final static String LOGIN_CHECK_PWD_ERROR = "密码错误，登录失败";
	public final static String LOGIN_CHECK_NAME_NOT_NULL = "用户名不能为空";
	public final static String LOGIN_CHECK_NAME_NOT_EXISTS = "用户不存在，不能进行登录";
	
	/**
	 * 项目模块的常量
	 */
	public final static String PROJECT_ADD = "添加项目";
	public final static String PROJECT_ADD_CODE_NOT_NULL = "项目编号不能为空";
	public final static String PROJECT_ADD_CODE_IS_EXISTS = "项目编号已存在";
	public final static String PROJECT_ADD_NAME_NOT_NULL = "项目名称不能为空";
	public final static String PROJECT_ADD_NAME_IS_EXISTS = "项目名称已存在";
	public final static String PROJECT_ADD_DATE_NOT_BEFORE = "项目结束时间不能在开始时间之前";
	public final static String PROJECT_ADD_BEGINDATE_NOT_NULL = "项目开始时间不能为空";
	public final static String PROJECT_ADD_ENDDATE_NOT_NUlL = "项目结束时间不能为空";
	public final static String PROJECT_ADD_VALID = "项目启用状态必须有效";
	public final static String PROJECT_ADD_SAVE_FALIED = "项目数据不能保存到数据库";
	public final static String PROJECT_DELETE = "删除项目";
	public final static String PROJECT_DELETE_ID_NOT_OR_NOT_EXISTS = "id为空或id代表的数据不存在";
	public final static String PROJECT_UPDATE = "更新项目";
	public final static String PROJECT_UPDATE_ID_NOT_NULL = "id不能为空";
	public final static String PROJECT_UPDATE_NOT_EXISTS = "id所代表的项目信息不存在";
	public final static String PROJECT_UPDATE_NOT_DATABASE = "项目信息不能更新到数据库中";
	/**
	 * 团队模块的常量
	 */
	public final static String TEAM_ADD = "添加团队";
	public final static String TEAM_ADD_NAME_NOT_NULL = "团队名字不能为空";
	public final static String TEAM_ADD_NAME_EXISTS = "团队名字已经存在";
	public final static String TEAM_ADD_NAME_FORMATE = "团队名字不符合命名格式，仅支持中文、数字、英文、下划线";
	public final static String TEAM_ADD_PROJECTID_NOT_NULL = "团队所属项目不能为空";
	public final static String TEAM_ADD_VALID_NOT_NULL = "团队状态错误";
	public final static String TEAM_ADD_SAVE_FAILED = "团队信息不能保存到数据库";
	public final static String TEAM_DELETE = "删除团队";
	public final static String TEAM_DELETE_NOT_EXISTS = "id所代表的团队信息不存在";
	public final static String TEAM_UPDATE = "更新团队";
	/**
	 * 产品模块的常量
	 */
	public final static String PRODUCT_ADD = "添加产品";
	public final static String PRODUCT_ADD_CODE_NOT_NULL = "产品编号不能为空";
	public final static String PRODUCT_ADD_CODE_IS_EXISTS = "产品编号已存在";
	public final static String PRODUCT_ADD_NAME_NOT_NULL = "产品名称不能为空";
	public final static String PRODUCT_ADD_NAME_IS_EXISTS = "产品名称已存在";
	public final static String PRODUCT_ADD_TEAM_NOT_EXISTS = "所选团队不能为空或不存在";
	public final static String PRODUCT_ADD_CLASSES_NOT_EXISTS = "所选分类不能为空或不存在";
	public final static String PRODUCT_ADD_DATE_NOT_NULL = "上下架时间不能为空";
	public final static String PRODUCT_ADD_DATE_BEFORE = "下架时间不能在上架时间之前";
	public final static String PRODUCT_DELETE = "删除产品";
	public final static String PRODUCT_DELETE_ID_NOT_FOUND = "id所代表的产品信息不存在";
	
	/**
	 * 分类模块的常量
	 */
	public final static String CLASSES_ADD = "添加分类";
	public final static String CLASSES_ADD_NAME_NOT_NULL = "分类名称不能为空";
	public final static String CLASSES_ADD_NAME_IS_EXISTS = "分类名称已存在";
	public final static String CLASSES_ADD_SORTID_NOT_NULL = "分类序号不能为空";
	public final static String CLASSES_ADD_SORTID_IS_EXISTS = "分类序号格式错误,仅支持自然数字";
	public final static String CLASSES_ADD_PARENTID_NOT_NULL = "父类id不能为空";
	public final static String CLASSES_ADD_SAVE_FAILED = "保存到数据库失败";
	public final static String CLASSES_DELETE = "删除分类";
	public final static String CLASSES_DELETE_DATA_NOT_EXISTS = "id所代表的分类信息不存在";
	public final static String CLASSES_DELETE_HAVE_CHILD = "此分类下有子类信息，不能删除";
	public final static String CLASSES_UPDATE = "更新分类";
	
	/**
	 * 角色模块的常量
	 */
	public final static String ROLE_ADD = "添加角色";
	public final static String ROLE_ADD_NAME_NOT_NULL = "角色名称不能为空";
	public final static String ROLE_ADD_NAME_IS_EXISTS = "角色名称已存在";
	public final static String ROLE_ADD_TEAMID_NOT_NULL = "角色所属团队不能为空";
	public final static String ROLE_ADD_ROLE_NOT_NULL = "角色所属身份不能为空";
	public final static String ROLE_ADD_EMAIL_ERROR = "邮箱输入错误";
	public final static String ROLE_ADD_MOBILE_NOT_NULL = "手机号不能为空";
	public final static String ROLE_ADD_MOBILE_ERROR = "手机号输入错误";
	public final static String ROLE_ADD_SAVE_FAILED = "角色信息保存到数据库失败";
	public final static String ROLE_DELETE = "删除角色";
	public final static String ROLE_DELETE_FAILED = "删除角色信息失败";
	public final static String ROLE_UPDATE = "修改角色";
	public final static String ROLE_UPDATE_IS_NOT_EXISTS = "该id所代表的角色信息不存在";
	
	/**
	 * 系统用户模块的常量
	 */
	public final static String USER_ADD = "添加用户";
	public final static String USER_ADD_USRENAME_NOT_NULL = "用户名不能为空";
	public final static String USER_ADD_USRENAME_IS_EXISTS = "用户名已存在";
	public final static String USER_ADD_PASSWORD_NOT_NULL = "用户密码不能为空";
	public final static String USER_ADD_PERMISSION_NOT_NULL = "用户权限不能为空";
	public final static String USER_ADD_SAVE_FAILED = "用户信息保存到数据库失败";
	public final static String USER_DELETE = "删除用户";
	public final static String USER_DELETE_FAILED = "删除用户失败";
	public final static String USER_UPDATE = "修改用户";
	@Deprecated
	public final static String USER_UPDATE_NOT_FOUNT ="该用户不存在";
	public final static String USER_UPDATE_NOT_FOUND ="该用户不存在";
	@Deprecated
	public final static String USER_UPDTE_PWD = "修改超级管理员密码";
	public final static String USER_UPDATE_PWD = "修改超级管理员密码";
	public final static String USER_UPDATE_PWD_NOT_PERMISSION = "只有超级管理员才有修改密码的权限";
	public final static String USER_OPERATOR_NOT_EXISTS = "操作用户不存在";
	public final static String USER_OPERATOR_NOT_PERMISSION = "当前用户没有操作权限";
	public final static String USER_NOT_LOGIN = "用户未登录";
	public final static String USER_ADMIN_OLD_PASSWORD_EMPTY = "超级管理员原密码不能为空";
	public final static String USER_ADMIN_OLD_PASSWORD_ERROR = "超级管理员原密码输入错误";
	public final static String USER_ADMIN_NEW_PASSWORD_EMPTY = "超级管理员新密码不能为空";
}
