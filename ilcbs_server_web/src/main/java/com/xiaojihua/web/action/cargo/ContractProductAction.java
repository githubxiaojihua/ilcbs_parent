package com.xiaojihua.web.action.cargo;

import com.opensymphony.xwork2.ModelDriven;
import com.xiaojihua.domain.ContractProducts;
import com.xiaojihua.domain.Factory;
import com.xiaojihua.service.ContractProductService;
import com.xiaojihua.service.FactoryService;
import com.xiaojihua.utils.Page;
import com.xiaojihua.web.action.BaseAction;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
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
import javax.persistence.metamodel.EntityType;
import java.util.List;

@Namespace("/cargo")
//传递参数为contract.id=${contract.id}是因为在到达货物新增和列表页的时候已经在在model模型
// contractProducts中已经存在contract.id
//并且在到达contractProductAction_tocreate的时候必须有contractId，因为页面查询的是某个合同下的数据
@Result(name = "tocreate", location = "contractProductAction_tocreate?contract.id=${contract.id}", type = "redirectAction")
public class ContractProductAction extends BaseAction implements ModelDriven<ContractProducts> {

    private ContractProducts contractProducts = new ContractProducts();

    @Override
    public ContractProducts getModel() {
        return contractProducts;
    }

    private Page page = new Page();

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Autowired
    private ContractProductService service;

    @Autowired
    private FactoryService factoryService;

    @Action(value="contractProductAction_tocreate",results={@Result(name = "tocreate", location = "/WEB-INF/pages/cargo/contract/jContractProductCreate.jsp")})
    public String toCreate(){

        // 1.查询factoryList所有的工厂列表，只要货物的
        Specification<Factory> spec = new Specification<Factory>() {

            @Override
            public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                return cb.equal(root.get("ctype").as(String.class), "货物");
            }
        };
        List<Factory> factoryList = factoryService.find(spec);
        super.put("factoryList", factoryList);

        // 2.查询同一个购销合同下的所有货物
        Specification<ContractProducts> spec1 = new Specification<ContractProducts>() {

            @Override
            public Predicate toPredicate(Root<ContractProducts> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                /*
                 * Join<Contract, ContractProduct> join = root.join("contract");
                 * cb.equal(join.get("id").as(String.class),
                 * model.getContract().getId());
                 * 关于这一块可以查询第八天笔记中的两篇word文档
                 * spring data jpa 中的Specification查询以及其他知识和JPA的Criteria查询API.doc
                 */
                return cb.equal(root.get("contract").get("id").as(String.class), contractProducts.getContract().getId());

            }
        };

        org.springframework.data.domain.Page<ContractProducts> page2 = service.findPage(spec1,
                new PageRequest(page.getPageNo() - 1, page.getPageSize()));
        page.setTotalPage(page2.getTotalPages());
        page.setResults(page2.getContent());
        page.setTotalRecord(page2.getTotalElements());
        page.setUrl("contractProductAction_tocreate");

        super.push(page);

        return "tocreate";
    }

    @Action(value = "contractProductAction_insert")
    public String insert() throws Exception {
        // TODO Auto-generated method stub
        service.saveOrUpdate(contractProducts);
        return "tocreate";
    }


    @Action(value = "contractProductAction_toupdate", results = {
            @Result(name = "toupdate", location = "/WEB-INF/pages/cargo/contract/jContractProductUpdate.jsp") })
    public String toupdate() throws Exception {
        // TODO Auto-generated method stub
        ContractProducts contractProduct = service.get(contractProducts.getId());
        super.push(contractProduct);

        // 1.查询factoryList所有的工厂列表，只要货物的
        Specification<Factory> spec = new Specification<Factory>() {

            @Override
            public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                return cb.equal(root.get("ctype").as(String.class), "货物");
            }
        };
        List<Factory> factoryList = factoryService.find(spec);
        super.put("factoryList", factoryList);

        return "toupdate";
    }

    @Action(value = "contractProductAction_update")
    public String update() throws Exception {
        // TODO Auto-generated method stub
        ContractProducts contractProduct = service.get(contractProducts.getId());
        contractProduct.setFactory(contractProducts.getFactory());
        contractProduct.setFactoryName(contractProducts.getFactoryName());
        contractProduct.setProductNo(contractProducts.getProductNo());
        contractProduct.setProductImage(contractProducts.getProductImage());
        contractProduct.setCnumber(contractProducts.getCnumber());
        contractProduct.setPackingUnit(contractProducts.getPackingUnit());
        contractProduct.setLoadingRate(contractProducts.getLoadingRate());
        contractProduct.setBoxNum(contractProducts.getBoxNum());
        contractProduct.setPrice(contractProducts.getPrice());
        contractProduct.setOrderNo(contractProducts.getOrderNo());
        contractProduct.setProductDesc(contractProducts.getProductDesc());
        contractProduct.setProductRequest(contractProducts.getProductRequest());


        service.saveOrUpdate(contractProduct);

        return "tocreate";
    }

    @Action(value = "contractProductAction_delete")
    public String delete() throws Exception {
        // TODO Auto-generated method stub
        service.deleteById(contractProducts.getId());
        return "tocreate";
    }
}
