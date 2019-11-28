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

@Namespace("/sysadmin")
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
        org.springframework.data.domain.Page<Dept> page2 = service.findPage(null, new PageRequest(page.getPageNo() - 1, page.getPageSize()));

        page.setTotalPage(page2.getTotalPages());   //总页数
        page.setTotalRecord(page2.getTotalElements()); //总记录数
        page.setResults(page2.getContent());      //查询的记录
        page.setUrl("deptAction_list");               //上下页跳转的地址(相对路径地址)

        super.push(page);

        return "list";
    }

}
