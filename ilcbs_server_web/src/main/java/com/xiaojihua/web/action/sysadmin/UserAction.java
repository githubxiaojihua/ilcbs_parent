package com.xiaojihua.web.action.sysadmin;

import com.opensymphony.xwork2.ModelDriven;
import com.xiaojihua.domain.Dept;
import com.xiaojihua.domain.Role;
import com.xiaojihua.domain.User;
import com.xiaojihua.service.DeptService;
import com.xiaojihua.service.RoleService;
import com.xiaojihua.service.UserService;
import com.xiaojihua.utils.Page;
import com.xiaojihua.web.action.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Namespace("/sysadmin")
@Result(name="alist",type="redirectAction",location="userAction_list")
public class UserAction extends BaseAction<User> implements ModelDriven<User> {

    @Autowired
    private UserService service;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    private User user = new User();
    @Override
    public User getModel() {
        return user;
    }

    private Page<User> page = new Page<>();

    public Page<User> getPage() {
        return page;
    }

    public void setPage(Page<User> page) {
        this.page = page;
    }

    private String[] roleIds;

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    @Action(value="userAction_list",results={@Result(name="list",location="/WEB-INF/pages/sysadmin/user/jUserList.jsp")})
    public String list(){

        org.springframework.data.domain.Page<User> japPage = service.findPage(null, new PageRequest(this.page.getPageNo() - 1, this.page.getPageSize()));
        page.setTotalPage(japPage.getTotalPages());//设置总页数
        page.setTotalRecord(japPage.getTotalElements());//设置总记录数
        page.setResults(japPage.getContent());//设置记录集
        page.setUrl("userAction_list");//设置跳转路径

        super.push(page);
        return "list";
    }


    @Action(value="userAction_toview",results={@Result(name="toView",location="/WEB-INF/pages/sysadmin/user/jUserView.jsp")})
    public String toView(){
        User user = service.get(this.user.getId());
        super.push(user);
        return "toView";
    }

    @Action(value="userAction_tocreate",results={@Result(name="toCreate",location="/WEB-INF/pages/sysadmin/user/jUserCreate.jsp")})
    public String toCreate(){
        //查询部门list
        Specification<Dept> deptSpec = new Specification<Dept>() {
            @Override
            public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("state").as(Integer.class),1);
            }
        };
        List<Dept> deptList = deptService.find(deptSpec);
        super.put("deptList",deptList);

        //查询上级领导
        Specification<User> userSpec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("state").as(Integer.class),1);
            }
        };
        List<User> userList = service.find(userSpec);
        super.put("userList",userList);
        return "toCreate";
    }

    @Action(value="userAction_insert")
    public String insert(){
        service.saveOrUpdate(user);
        return "alist";
    }

    @Action(value="userAction_toupdate",results={@Result(name="toUpdate",location="/WEB-INF/pages/sysadmin/user/jUserUpdate.jsp")})
    public String toUpdate(){
        User user = service.get(this.user.getId());
        super.push(user);

        Specification<Dept> spec = new Specification<Dept>() {
            @Override
            public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("state").as(Integer.class),1);
            }
        };

        List<Dept> list = deptService.find(spec);
        super.put("deptList",list);
        return "toUpdate";
    }


    @Action(value="userAction_update")
    public String update(){
        User userDb = service.get(this.user.getId());
        userDb.setDept(user.getDept());
        userDb.getUserinfo().setName(user.getUserinfo().getName());
        userDb.setState(user.getState());
        service.saveOrUpdate(userDb);
        return "alist";
    }

    @Action(value="userAction_delete")
    public String delete(){
        String[] ids = user.getId().split(", ");
        service.delete(ids);
        return "alist";
    }

    @Action(value="userAction_torole",results={@Result(name="toRole",location="/WEB-INF/pages/sysadmin/user/jUserRole.jsp")})
    public String toRole(){
        User user = service.get(this.user.getId());
        super.push(user);

        Set<Role> roles = user.getRoles();
        StringBuilder sb = new StringBuilder();
        for(Role role : roles){
            sb.append(role.getName()).append(",");
        }
        super.put("roleStr",sb);

        List<Role> roleList = roleService.find(null);
        super.put("roleList",roleList);

        return "toRole";
    }

    @Action(value="userAction_role")
    public String role(){
        User user = service.get(this.user.getId());
        Set<Role> roles = new HashSet<>();
        for(String id : roleIds){
            Role role = roleService.get(id);
            roles.add(role);
        }
        user.setRoles(roles);
        service.saveOrUpdate(user);
        return "alist";
    }


}
