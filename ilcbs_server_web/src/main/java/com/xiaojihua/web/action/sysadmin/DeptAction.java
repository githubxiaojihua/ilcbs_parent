package com.xiaojihua.web.action.sysadmin;

import com.opensymphony.xwork2.ModelDriven;
import com.xiaojihua.domain.Dept;
import com.xiaojihua.service.DeptService;
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
import java.util.List;

@Namespace("/sysadmin")
@Result(name="alist",type="redirectAction",location="deptAction_list")
public class DeptAction extends BaseAction<Dept> implements ModelDriven<Dept> {

    @Autowired
    private DeptService service;

    private Dept dept = new Dept();
    @Override
    public Dept getModel() {
        return dept;
    }

    /**
     * 这是自己的page类，不是spring data jpa的
     * 用于接收spring data jpa 的page的一些数据
     * 形成自己的page，用于在前台显示
     * 此page类也是一个组件，能在前台直接显示分页组件样式
     */
    private Page page = new Page();

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }


    @Action(value="deptAction_list",results={@Result(name="list",location="/WEB-INF/pages/sysadmin/dept/jDeptList.jsp")})
    public String list() throws Exception {
        // TODO Auto-generated method stub
        //page.getPageNo() - 1是因为spring data jap 的page组件是从0开始的，而我们为了前端显示是从1开始的
        org.springframework.data.domain.Page<Dept> page2 = service.findPage(null, new PageRequest(page.getPageNo() - 1, page.getPageSize()));

        page.setTotalPage(page2.getTotalPages());   //总页数
        page.setTotalRecord(page2.getTotalElements()); //总记录数
        page.setResults(page2.getContent());      //查询的记录
        //相对路径是根据前台页面当前所在的目录而言的，因此可以写相对路径，当前页面的所在目录有可能是没有显示
        //在地址栏的，因为牵扯到服务器跳转，但是他始终会有一个相对路径的，比如请求了当前的ACTION那么相对路径
        //就是ACTION所在的目录
        //当一开始点击左侧部门管理的链接时候跳转了applicationContext/sysadmin/deptAction_list
        //因此当前路径变为了applicationContext/sysadmin
        page.setUrl("deptAction_list");               //上下页跳转的地址(相对路径地址)

        super.push(page);

        return "list";
    }

    /**
     * 去查看页面
     * @return
     */
    @Action(value="deptAction_toview",results={@Result(name="toView",location="/WEB-INF/pages/sysadmin/dept/jDeptView.jsp")})
    public String to_view(){
        Dept dept = service.get(this.dept.getId());
        super.push(dept);
        return "toView";
    }


    /**
     * 去新增页面
     * @return
     */
    @Action(value="deptAction_tocreate",results={@Result(name="toCreate",location="/WEB-INF/pages/sysadmin/dept/jDeptCreate.jsp")})
    public String toCreate(){
        //关于Specification的用法可以参考：https://www.jianshu.com/p/659e9715d01d
        Specification<Dept> specification = new Specification<Dept>() {
            @Override
            public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("state").as(Integer.class),1);
            }
        };

        List<Dept> list = service.find(specification);
        super.put("deptList",list);
        return "toCreate";
    }

    /**
     * 插入
     * @return
     */
    @Action(value="deptAction_insert")
    public String insert(){
        /*
            使用了模型驱动收集到了新增页面中DEPT类中的所需数据
            其中包含父部门ID，使用的OGNL表达式
         */
        service.saveOrUpdate(dept);
        //跳转到List页面刷新
        return "alist";
    }


    /**
     * 去修改页面
     * @return
     */
    @Action(value="deptAction_toupdate",results={@Result(name="toUpdate",location="/WEB-INF/pages/sysadmin/dept/jDeptUpdate.jsp")})
    public String toUpdate(){
        Dept dept = service.get(this.dept.getId());
        super.push(dept);

        Specification<Dept> specification = new Specification<Dept>() {
            @Override
            public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("state").as(Integer.class),1);
            }
        };
        List<Dept> list = service.find(specification);
        list.remove(dept);
        super.put("deptList",list);

        return "toUpdate";
    }

    /**
     * 更新
     * @return
     */
    @Action(value="deptAction_update")
    public String update(){
        Dept dept = service.get(this.dept.getId());
        dept.setParent(this.dept.getParent());//parent只设置了id是可以更新的而且只有这样才能更新
        dept.setDeptName(this.dept.getDeptName());
        service.saveOrUpdate(dept);
        return "alist";
    }

    /**
     * 可以使用递归删除，来解决存在子部门的问题，
     * 现在不做实现，如果有子部门是不允许删除的
     * 删除的时候会提示外键错误
     * @return
     */
    @Action(value="deptAction_delete")
    public String delete(){
        System.out.println(dept.getId());
        //当前端页面传递了多个name相同的属性给模型驱动的属性时候，如果是String类型的话
        //struts会使用", "进行组合，所以要进行分割
        String[] ids = dept.getId().split(", ");

        service.delete(ids);
        return "alist";
    }

}
