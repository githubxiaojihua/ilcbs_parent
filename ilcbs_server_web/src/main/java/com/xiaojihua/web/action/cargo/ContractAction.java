package com.xiaojihua.web.action.cargo;

import com.opensymphony.xwork2.ModelDriven;
import com.xiaojihua.domain.Contract;
import com.xiaojihua.domain.User;
import com.xiaojihua.service.ContractService;
import com.xiaojihua.utils.Page;
import com.xiaojihua.utils.SysConstant;
import com.xiaojihua.web.action.BaseAction;
import org.apache.http.HttpResponse;
import org.apache.struts2.ServletActionContext;
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
import javax.servlet.http.HttpServletResponse;

@Namespace("/cargo")
@Result(name="alist",location="contractAction_list",type="redirectAction")
public class ContractAction extends BaseAction implements ModelDriven<Contract> {

    private Contract contract = new Contract();
    @Override
    public Contract getModel() {
        return contract;
    }

    private Page page = new Page();

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Autowired
    private ContractService servcie;

    @Action(value="contractAction_list",results={@Result(name="list",location="/WEB-INF/pages/cargo/contract/jContractList.jsp")})
    public String list(){

        final User user = (User) session.get(SysConstant.CURRENT_USER_INFO);

        Specification<Contract> spec = new Specification<Contract>() {

            @Override
            public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                Predicate p = null;
                switch (user.getUserinfo().getDegree()) {
                    case 0:  // 管理员

                        break;
                    case 1: // 副总

                        break;
                    case 2:  // 部门总经理

                        break;
                    case 3:  // 部门经理
                        p = cb.equal(root.get("createDept").as(String.class), user.getDept().getId());
                        break;
                    default: // 4.普通员工
                        //select * from Contract_c where createBy = '自己的id'
                        p = cb.equal(root.get("createBy").as(String.class), user.getId());
                        break;
                }
                return p;
            }
        };

        org.springframework.data.domain.Page<Contract> page1 = servcie.findPage(spec, new PageRequest(this.page.getPageNo() - 1, this.page.getPageSize()));

        page.setTotalPage(page1.getTotalPages());
        page.setTotalRecord(page1.getTotalElements());
        page.setResults(page1.getContent());
        page.setUrl("contractAction_list");


        super.push(page);
        return "list";
    }

    @Action(value="contractAction_toview",results={@Result(name="toview",location="/WEB-INF/pages/cargo/contract/jContractView.jsp")})
    public String toView(){
        Contract contract = servcie.get(this.contract.getId());
        super.push(contract);
        return "toview";
    }

    @Action(value="contractAction_tocreate",results={@Result(name="tocreate",location="/WEB-INF/pages/cargo/contract/jContractCreate.jsp")})
    public String toCreate(){
        return "tocreate";
    }

    @Action(value="contractAction_insert")
    public String insert(){
        User user = (User) session.get(SysConstant.CURRENT_USER_INFO);
        contract.setCreateBy(user.getId());
        contract.setCreateDept(user.getDept().getId());
        servcie.saveOrUpdate(contract);
        return "alist";
    }

    @Action(value="contractAction_toupdate",results={@Result(name="toupdate",location="/WEB-INF/pages/cargo/contract/jContractUpdate.jsp")})
    public String toupdate() throws Exception {
        // TODO Auto-generated method stub
        Contract contract = servcie.get(this.contract.getId());
        super.push(contract);

        return "toupdate";
    }

    @Action(value="contractAction_update")
    public String update() throws Exception {
        // TODO Auto-generated method stub
        Contract contract1 = servcie.get(contract.getId());
        contract1.setCustomName(contract.getCustomName());
        contract1.setPrintStyle(contract.getPrintStyle());
        contract1.setContractNo(contract.getContractNo());
        contract1.setOfferor(contract.getOfferor());
        contract1.setInputBy(contract.getInputBy());
        contract1.setCheckBy(contract.getCheckBy());
        contract1.setInspector(contract.getInspector());
        contract1.setSigningDate(contract.getSigningDate());
        contract1.setImportNum(contract.getImportNum());
        contract1.setShipTime(contract.getShipTime());
        contract1.setTradeTerms(contract.getTradeTerms());
        contract1.setDeliveryPeriod(contract.getDeliveryPeriod());
        contract1.setCrequest(contract.getCrequest());
        contract1.setRemark(contract.getRemark());

        servcie.saveOrUpdate(contract1);

        return "alist";
    }

    @Action(value="contractAction_submit")
    public String submit(){
        changeStatus(1);
        return "alist";
    }

    @Action(value="contractAction_cancel")
    public String cancel(){
        changeStatus(0);
        return "alist";
    }

    private void changeStatus(int status){
        String[] ids = contract.getId().split(", ");
        for (String cid : ids) {
            Contract contract = servcie.get(cid);
            contract.setState(status);
            servcie.saveOrUpdate(contract);
        }
    }

    @Action(value="contractAction_delete")
    public String delete(){
        String[] ids = contract.getId().split(", ");
        servcie.delete(ids);
        return "alist";
    }

    @Action(value="contractAction_print")
    public String print() throws Exception {

        Contract contract = servcie.get(this.contract.getId());
        ContractPrint print = new ContractPrint();

        String path = ServletActionContext.getServletContext().getRealPath("/");
        HttpServletResponse response = ServletActionContext.getResponse();
        print.print(contract,path,response);
        return NONE;
    }
}
