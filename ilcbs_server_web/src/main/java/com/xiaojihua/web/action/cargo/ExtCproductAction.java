package com.xiaojihua.web.action.cargo;

import com.opensymphony.xwork2.ModelDriven;
import com.xiaojihua.domain.ContractProducts;
import com.xiaojihua.domain.ExtCproduct;
import com.xiaojihua.domain.Factory;
import com.xiaojihua.service.ContractProductService;
import com.xiaojihua.service.ExtCproductService;
import com.xiaojihua.service.FactoryService;
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

@Namespace("/cargo")
//传递参数为contract.id=${contract.id}是因为在到达货物新增和列表页的时候已经在在model模型
// contractProducts中已经存在contract.id
//并且在到达contractProductAction_tocreate的时候必须有contractId，因为页面查询的是某个合同下的数据
@Result(name="alist",location="extCproductAction_tocreate?contractProduct.id=${contractProduct.id}",type="redirectAction")
public class ExtCproductAction extends BaseAction implements ModelDriven<ExtCproduct> {

    private ExtCproduct extCproduct = new ExtCproduct();
    @Override
    public ExtCproduct getModel() {
        return extCproduct;
    }

    private Page page = new Page();

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Autowired
    private ExtCproductService extCproductService;

    @Autowired
    private FactoryService factoryService;

    @Action(value="extCproductAction_tocreate",results={@Result(name="toCreate",location="/WEB-INF/pages/cargo/contract/jExtCproductCreate.jsp")})
    public String toCreate(){

        Specification<Factory> factorySpecification = new Specification<Factory>() {

            @Override
            public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("ctype").as(String.class),"附件");
            }
        };
        List<Factory> factoryList = factoryService.find(factorySpecification);
        super.put("factoryList",factoryList);

        //关于Specification的相关查询可以参考
        Specification<ExtCproduct> extCproductSpecification = new Specification<ExtCproduct>() {

            @Override
            public Predicate toPredicate(Root<ExtCproduct> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("contractProduct").get("id").as(String.class),extCproduct.getContractProduct().getId());
            }
        };

        org.springframework.data.domain.Page<ExtCproduct> page1 = extCproductService.findPage(extCproductSpecification, new PageRequest(this.page.getPageNo() - 1, this.page.getPageSize()));
        page.setTotalPage(page1.getTotalPages());
        page.setTotalRecord(page1.getTotalElements());
        page.setResults(page1.getContent());
        page.setUrl("extCproductAction_tocreate");
        super.push(page);

        return "toCreate";
    }

    @Action(value="extCproductAction_insert")
    public String insert() throws Exception {
        // TODO Auto-generated method stub
        extCproductService.saveOrUpdate(extCproduct);
        return "alist";
    }

    @Action(value="extCproductAction_toupdate",results={@Result(name="toupdate",location="/WEB-INF/pages/cargo/contract/jExtCproductUpdate.jsp")})
    public String toupdate() throws Exception {
        // TODO Auto-generated method stub
        ExtCproduct extCproduct = extCproductService.get(this.extCproduct.getId());
        super.push(extCproduct);

        Specification<Factory> spec2 = new Specification<Factory>() {

            @Override
            public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                return cb.equal(root.get("ctype").as(String.class), "附件");
            }
        };

        List<Factory> factoryList = factoryService.find(spec2);

        super.put("factoryList", factoryList);

        return "toupdate";
    }

    @Action(value="extCproductAction_update")
    public String update() throws Exception {
        // TODO Auto-generated method stub

        ExtCproduct extCproduct = extCproductService.get(this.extCproduct.getId());
        extCproduct.setFactory(this.extCproduct.getFactory());
        extCproduct.setFactoryName(this.extCproduct.getFactoryName());
        extCproduct.setProductNo(this.extCproduct.getProductNo());
        extCproduct.setProductImage(this.extCproduct.getProductImage());
        extCproduct.setCnumber(this.extCproduct.getCnumber());
        extCproduct.setPackingUnit(this.extCproduct.getPackingUnit());
        extCproduct.setPrice(this.extCproduct.getPrice());
        extCproduct.setOrderNo(this.extCproduct.getOrderNo());
        extCproduct.setProductDesc(this.extCproduct.getProductDesc());
        extCproduct.setProductRequest(this.extCproduct.getProductRequest());

        extCproductService.saveOrUpdate(extCproduct);

        return "alist";
    }

    @Action(value="extCproductAction_delete")
    public String delete() throws Exception {
        // TODO Auto-generated method stub
        extCproductService.deleteById(this.extCproduct.getId());
        return "alist";
    }
}
