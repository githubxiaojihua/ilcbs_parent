package com.xiaojihua.web.action.sysadmin;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.xiaojihua.domain.Role;
import com.xiaojihua.service.RoleService;
import com.xiaojihua.utils.Page;
import com.xiaojihua.web.action.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.opensymphony.xwork2.ModelDriven;

@Namespace("/sysadmin")
@Result(name = "alist", type = "redirectAction", location = "roleAction_list")
public class RoleAction extends BaseAction implements ModelDriven<Role> {

	@Autowired
	private RoleService roleService;

	private Role model = new Role();

	@Override
	public Role getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	private Page page = new Page();

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Action(value = "roleAction_list", results = {
			@Result(name = "list", location = "/WEB-INF/pages/sysadmin/role/jRoleList.jsp") })
	public String list() throws Exception {
		// TODO Auto-generated method stub

		org.springframework.data.domain.Page<Role> page2 = roleService.findPage(null,
				new PageRequest(page.getPageNo() - 1, page.getPageSize()));

		page.setTotalPage(page2.getTotalPages()); // 总页数
		page.setTotalRecord(page2.getTotalElements()); // 总记录数
		page.setResults(page2.getContent()); // 查询的记录
		page.setUrl("roleAction_list"); // 上下页跳转的地址(相对路径地址)

		super.push(page);

		return "list";
	}

	/**
	 * 查看单个角色
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "roleAction_toview", results = {
			@Result(name = "toview", location = "/WEB-INF/pages/sysadmin/role/jRoleView.jsp") })
	public String toview() throws Exception {
		// TODO Auto-generated method stub
		// 先根据id从数据库中查询
		Role role = roleService.get(model.getId());

		super.push(role);

		return "toview";
	}

	/**
	 * 去新增页面
	 */
	@Action(value = "roleAction_tocreate", results = {
			@Result(name = "tocreate", location = "/WEB-INF/pages/sysadmin/role/jRoleCreate.jsp") })
	public String tocreate() throws Exception {
		

		return "tocreate";
	}

	/**
	 * 新增方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "roleAction_insert")
	public String insert() throws Exception {
		// TODO Auto-generated method stub
		roleService.saveOrUpdate(model); // 查部门的默认状态

		return "alist";
	}

	/**
	 * 去修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "roleAction_toupdate", results = {
			@Result(name = "toupdate", location = "/WEB-INF/pages/sysadmin/role/jRoleUpdate.jsp") })
	public String toupdate() throws Exception {
		// TODO Auto-generated method stub
		// 1.根据id查询部门对象，压入值栈中
		Role role = roleService.get(model.getId());

		super.push(role);

		return "toupdate";
	}
	
	
	/**
	 * 删除修改方法
	 * @return
	 * @throws Exception
	 */
	@Action(value="roleAction_update")
	public String update() throws Exception {
		// TODO Auto-generated method stub
		// 根据id查询数据库的对象
		Role role = roleService.get(model.getId());
		role.setName(model.getName());
		role.setRemark(model.getRemark());
		
		roleService.saveOrUpdate(role);
		
		return "alist";
	}
	
	/**
	 * 角色删除
	 */
	@Action(value="roleAction_delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(model.getId());
		String[] ids = model.getId().split(", ");  //通过逗号空格切割成string数组
		
		roleService.delete(ids);
		
		return "alist";
	}
}
