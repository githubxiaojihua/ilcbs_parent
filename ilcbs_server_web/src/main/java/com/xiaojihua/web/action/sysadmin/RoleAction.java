package com.xiaojihua.web.action.sysadmin;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.xiaojihua.domain.Module;
import com.xiaojihua.domain.Role;
import com.xiaojihua.service.ModuleService;
import com.xiaojihua.service.RoleService;
import com.xiaojihua.utils.Page;
import com.xiaojihua.web.action.BaseAction;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
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

	@Autowired
	private ModuleService moduleService;

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

	private String moduleIds;

    public String getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(String moduleIds) {
        this.moduleIds = moduleIds;
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

	@Action(value="roleAction_tomodule",results={@Result(name="toModule",location="/WEB-INF/pages/sysadmin/role/jRoleModule.jsp")})
	public String toModule(){
        Role role = roleService.get(model.getId());
        super.push(role);
        return "toModule";
	}

	//这里使用手工拼装json字符串的方式
	//@Action(value="roleAction_genzTreeNodes")
	public String genzTreeNodes_old() throws Exception{
        // 返回ajax请求后的json字符串满足ztree的数据

        // 1。根据id获取角色对象
        Role role = roleService.get(model.getId());
        Set<Module> roleModules = role.getModules(); //用户所拥有的所有模块

        // 2.查询所有的模块
        Specification<Module> spec = new Specification<Module>() {

            @Override
            public Predicate toPredicate(Root<Module> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                return cb.equal(root.get("state").as(Integer.class), 1);
            }
        };
        List<Module> moduleList = moduleService.find(spec); //所有未停用的模块
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        //[{"id": "11","pId": "1","name": "随意勾选 1-1"}, {"id": "111","pId": "11","name": "随意勾选 1-1-1","checked": "true"}]

        int size = moduleList.size();
        for (Module module:moduleList) {
            size--;
            sb.append("{");
            sb.append("\"id\": \"").append(module.getId()).append("\"");
            sb.append(",\"pId\": \"").append(module.getParentId()).append("\"");
            sb.append(",\"name\": \"").append(module.getName()).append("\"");
            //是否当前角色拥有该模块
            if(roleModules.contains(module)){
                sb.append(",\"checked\": \"true\"");
            }

            sb.append("}");
            if(size > 0){
                sb.append(",");
            }
        }
        sb.append("]");

        System.out.println("=========="+sb.toString());

        //向前端写json字符串
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(sb.toString());
	    return NONE;
    }


    //使用fastjson，返回前端ajax请求
    @Action(value="roleAction_genzTreeNodes")
    public String genzTreeNodes() throws Exception{
        // 返回ajax请求后的json字符串满足ztree的数据

        // 1。根据id获取角色对象
        Role role = roleService.get(model.getId());
        Set<Module> roleModules = role.getModules(); //用户所拥有的所有模块

        // 2.查询所有的模块
        Specification<Module> spec = new Specification<Module>() {

            @Override
            public Predicate toPredicate(Root<Module> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                return cb.equal(root.get("state").as(Integer.class), 1);
            }
        };
        List<Module> moduleList = moduleService.find(spec); //所有未停用的模块

        List<Map<String,Object>> toJsonList = new ArrayList<>();
        //[{"id": "11","pId": "1","name": "随意勾选 1-1"}, {"id": "111","pId": "11","name": "随意勾选 1-1-1","checked": "true"}]
        for(Module m : moduleList){
            Map<String,Object> jsonMap = new HashMap<>();
            jsonMap.put("id",m.getId());
            jsonMap.put("pId",m.getParentId());
            jsonMap.put("name",m.getName());
            if(roleModules.contains(m)){
                jsonMap.put("checked",true);
            }
            toJsonList.add(jsonMap);
        }

        String jsonString = JSON.toJSONString(toJsonList);

        System.out.println("=========="+jsonString);

        //向前端写json字符串
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(jsonString);
        return NONE;
    }

    @Action(value="roleAction_module")
    public String module(){
        Role role = roleService.get(model.getId());

        Set<Module> modules = new HashSet<>();
        String[] ids = moduleIds.split(",");
        for(String id : ids){
            modules.add(moduleService.get(id));
        }

        role.setModules(modules);

        roleService.saveOrUpdate(role);

        return "alist";
    }
}
