package com.xiaojihua.web.action.cargo;

import cn.itcast.export.webservice.Exception_Exception;
import cn.itcast.export.webservice.IEpService;
import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.xiaojihua.domain.Contract;
import com.xiaojihua.domain.Export;
import com.xiaojihua.domain.ExportProduct;
import com.xiaojihua.service.ContractService;
import com.xiaojihua.service.ExportProductService;
import com.xiaojihua.service.ExportService;
import com.xiaojihua.utils.Page;
import com.xiaojihua.utils.UtilFuns;
import com.xiaojihua.vo.ExportProductResult;
import com.xiaojihua.vo.ExportProductVo;
import com.xiaojihua.vo.ExportResult;
import com.xiaojihua.vo.ExportVo;
import com.xiaojihua.web.action.BaseAction;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Result(name="alist",location="exportAction_list",type="redirectAction")
public class ExportAction extends BaseAction implements ModelDriven<Export> {

    private Export export = new Export();

    @Autowired
    private IEpService epService;

    @Override
    public Export getModel() {
        return export;
    }

    private Page page = new Page();

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Autowired
    private ExportService exportService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ExportProductService exportProductService;


    @Action(value="exportAction_contractList",results={@Result(name="contractList",location="/WEB-INF/pages/cargo/export/jContractList.jsp")})
    public String contractList(){

        //查询所有state为1的购销合同
        Specification<Contract> spec = new Specification<Contract>() {
            @Override
            public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("state").as(Integer.class),1);
            }
        };

        org.springframework.data.domain.Page<Contract> page1 = contractService.findPage(spec, new PageRequest(this.page.getPageNo() - 1, this.page.getPageSize()));

        page.setTotalPage(page1.getTotalPages());
        page.setTotalRecord(page1.getTotalElements());
        page.setResults(page1.getContent());
        page.setUrl("exportAction_contractList");

        super.push(page);
        return "contractList";
    }

    @Action(value="exportAction_tocreate",results={@Result(name="tocreate",location="/WEB-INF/pages/cargo/export/jExportCreate.jsp")})
    public String tocreate() throws Exception {
        // TODO Auto-generated method stub
        return "tocreate";
    }

    /**
     * 新增出口报运单
     * @return
     * @throws Exception
     */
    @Action(value="exportAction_insert")
    public String insert() throws Exception {
        // TODO Auto-generated method stub
        exportService.saveOrUpdate(export);
        return "alist";
    }

    /**
     * 出口报运单列表
     * @return
     * @throws Exception
     */
    @Action(value="exportAction_list",results={@Result(name="list",location="/WEB-INF/pages/cargo/export/jExportList.jsp")})
    public String list() throws Exception {
        // TODO Auto-generated method stub
        org.springframework.data.domain.Page<Export> page2 = exportService.findPage(null, new PageRequest(page.getPageNo() - 1, page.getPageSize()));

        page.setTotalPage(page2.getTotalPages());
        page.setTotalRecord(page2.getTotalElements());
        page.setResults(page2.getContent());
        page.setUrl("exportAction_list");

        super.push(page);

        return "list";
    }


    @Action(value="exportAction_toview",results={@Result(name="toview",location="/WEB-INF/pages/cargo/export/jExportView.jsp")})
    public String toview(){
        Export export = exportService.get(this.export.getId());
        super.push(export);
        return "toview";
    }

    @Action(value="exportAction_submit")
    public String submit(){
        String[] ids = this.export.getId().split(", ");
        for(String id : ids){
            Export export = exportService.get(id);
            export.setState(1);
            exportService.saveOrUpdate(export);
        }
        return "alist";
    }

    @Action(value="exportAction_cancel")
    public String cancle(){
        String[] ids = this.export.getId().split(", ");
        for(String id : ids){
            Export export = exportService.get(id);
            export.setState(0);
            exportService.saveOrUpdate(export);
        }
        return "alist";
    }

    @Action(value="exportAction_delete")
    public String delete(){
        String[] ids = this.export.getId().split(", ");
        exportService.delete(ids);
        return "alist";
    }


    @Action(value="exportAction_toupdate",results={@Result(name="toupdate",location="/WEB-INF/pages/cargo/export/jExportUpdate.jsp")})
    public String toupdate(){
        Export export = exportService.get(this.export.getId());
        super.push(export);
        return "toupdate";
    }

    @Action(value="exportAction_getTabledoData")
    public String getTabledoData() throws Exception {
        // TODO Auto-generated method stub

        Specification<ExportProduct> spec = new Specification<ExportProduct>() {

            @Override
            public Predicate toPredicate(Root<ExportProduct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                return cb.equal(root.get("export").get("id").as(String.class), export.getId());
            }
        };
        List<ExportProduct> epList = exportProductService.find(spec);

        ArrayList list = new ArrayList();
        for (ExportProduct exportProduct : epList) {
            HashMap map = new HashMap();
            map.put("id", exportProduct.getId());
            map.put("productNo", exportProduct.getProductNo());
            map.put("cnumber", exportProduct.getCnumber());
            map.put("grossWeight", UtilFuns.convertNull(exportProduct.getGrossWeight()));
            map.put("netWeight", UtilFuns.convertNull(exportProduct.getNetWeight()));
            map.put("sizeLength", UtilFuns.convertNull(exportProduct.getSizeLength()));
            map.put("sizeWidth", UtilFuns.convertNull(exportProduct.getSizeWidth()));
            map.put("sizeHeight", UtilFuns.convertNull(exportProduct.getSizeHeight()));
            map.put("exPrice", UtilFuns.convertNull(exportProduct.getExPrice()));
            map.put("tax", UtilFuns.convertNull(exportProduct.getTax()));

            list.add(map);
        }

        String jsonString = JSON.toJSONString(list);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(jsonString);

        return NONE;
    }


    /**
     * 修改逻辑
     * @return
     * @throws Exception
     */
    @Action(value="exportAction_update")
    public String update() throws Exception {
        // TODO Auto-generated method stub

        // 出口报运单的保存
        Export export = exportService.get(this.export.getId());
        export.setInputDate(this.export.getInputDate());
        export.setLcno(this.export.getLcno());
        export.setConsignee(this.export.getConsignee());
        export.setShipmentPort(this.export.getShipmentPort());
        export.setDestinationPort(this.export.getDestinationPort());
        export.setTransportMode(this.export.getTransportMode());
        export.setPriceCondition(this.export.getPriceCondition());
        export.setMarks(this.export.getMarks());
        export.setRemark(this.export.getRemark());

        exportService.saveOrUpdate(export);

        // 出口报运修改的商品
        for (int i = 0; i < mr_id.length ; i++) {
            if(mr_changed[i].equals("1")){  // 修改过的进行操作 1代表修改
                ExportProduct exportProduct = exportProductService.get(mr_id[i]);
                exportProduct.setCnumber(mr_cnumber[i]);
                exportProduct.setGrossWeight(mr_grossWeight[i]);
                exportProduct.setNetWeight(mr_netWeight[i]);
                exportProduct.setSizeLength(mr_sizeLength[i]);
                exportProduct.setSizeWidth(mr_sizeWidth[i]);
                exportProduct.setSizeHeight(mr_sizeHeight[i]);
                exportProduct.setExPrice(mr_exPrice[i]);
                exportProduct.setTax(mr_tax[i]);

                exportProductService.saveOrUpdate(exportProduct);
            }
        }

        return "alist";
    }

    private String[] mr_id;
    private String[] mr_changed;
    private Integer[] mr_cnumber;
    private Double[] mr_grossWeight;
    private Double[] mr_netWeight;
    private Double[] mr_sizeLength;
    private Double[] mr_sizeWidth;
    private Double[] mr_sizeHeight;
    private Double[] mr_exPrice;
    private Double[] mr_tax;

    public String[] getMr_id() {
        return mr_id;
    }

    public void setMr_id(String[] mr_id) {
        this.mr_id = mr_id;
    }

    public String[] getMr_changed() {
        return mr_changed;
    }

    public void setMr_changed(String[] mr_changed) {
        this.mr_changed = mr_changed;
    }

    public Integer[] getMr_cnumber() {
        return mr_cnumber;
    }

    public void setMr_cnumber(Integer[] mr_cnumber) {
        this.mr_cnumber = mr_cnumber;
    }

    public Double[] getMr_grossWeight() {
        return mr_grossWeight;
    }

    public void setMr_grossWeight(Double[] mr_grossWeight) {
        this.mr_grossWeight = mr_grossWeight;
    }

    public Double[] getMr_netWeight() {
        return mr_netWeight;
    }

    public void setMr_netWeight(Double[] mr_netWeight) {
        this.mr_netWeight = mr_netWeight;
    }

    public Double[] getMr_sizeLength() {
        return mr_sizeLength;
    }

    public void setMr_sizeLength(Double[] mr_sizeLength) {
        this.mr_sizeLength = mr_sizeLength;
    }

    public Double[] getMr_sizeWidth() {
        return mr_sizeWidth;
    }

    public void setMr_sizeWidth(Double[] mr_sizeWidth) {
        this.mr_sizeWidth = mr_sizeWidth;
    }

    public Double[] getMr_sizeHeight() {
        return mr_sizeHeight;
    }

    public void setMr_sizeHeight(Double[] mr_sizeHeight) {
        this.mr_sizeHeight = mr_sizeHeight;
    }

    public Double[] getMr_exPrice() {
        return mr_exPrice;
    }

    public void setMr_exPrice(Double[] mr_exPrice) {
        this.mr_exPrice = mr_exPrice;
    }

    public Double[] getMr_tax() {
        return mr_tax;
    }

    public void setMr_tax(Double[] mr_tax) {
        this.mr_tax = mr_tax;
    }


    @Action(value="exportAction_exportE")
    public String exportE() throws Exception_Exception {
        WebClient client = WebClient.create("http://localhost:8090/jkexportrs/ws/export/user");
        client.type(javax.ws.rs.core.MediaType.APPLICATION_JSON);
        // 出口报运单对象
        Export export = exportService.get(this.export.getId());
        ExportVo exportVo = new ExportVo();
        // 拷贝报运单对象
        BeanUtils.copyProperties(export,exportVo);
        exportVo.setExportId(export.getId());
        // 拷贝报运单中的产品
        Set<ExportProduct> exportProducts = export.getExportProducts();
        HashSet<ExportProductVo> exportProductsVo = new HashSet<>();
        for(ExportProduct ep : exportProducts){
            ExportProductVo exportProductVo = new ExportProductVo();
            BeanUtils.copyProperties(ep,exportProductVo);
            exportProductVo.setExportProductId(ep.getId());
            exportProductVo.setExportId(export.getId() );
            exportProductsVo.add(exportProductVo);
        }
        exportVo.setProducts(exportProductsVo);
        //提交数据
        client.post(exportVo);


        WebClient returnClient = WebClient.create("http://localhost:8090/jkexportrs/ws/export/user/" + exportVo.getId());
        returnClient.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON);
        ExportResult exportResult = returnClient.get(ExportResult.class);

        //根据返回的id更新本地对象
        Export exportDb = exportService.get(exportResult.getExportId());
        exportDb.setState(exportResult.getState());
        exportDb.setRemark(exportResult.getRemark());
        exportService.saveOrUpdate(exportDb);

        Set<ExportProductResult> products = exportResult.getProducts();
        for(ExportProductResult epResult : products){
            ExportProduct exportProduct = exportProductService.get(epResult.getExportProductId());
            exportProduct.setTax(epResult.getTax());
            exportProductService.saveOrUpdate(exportProduct);
        }
        return "alist";
    }

    /**
     * 使用一个FastJson的注解可以将本地Export类转换成海关系统中要求的JSON格式
     * 仍然是jax_ws风格的
     * @return
     * @throws Exception_Exception
     */
    @Action(value="exportAction_fastjson_exportE")
    public String fastjson_exportE() throws Exception_Exception {
        // 出口报运单对象
        Export export = exportService.get(this.export.getId());

        String jsonString = JSON.toJSONString(export);
        System.out.println("=======" + jsonString);
        //调用海关webservice接口获取海关处理后返回的数据
        /**
         * 这里调用的是通过ilcbs_server_client模块来调用海关jax_ws风格的系统
         * 并返回其处理完毕后的json字符串
         */
        //通过fastjson将对象转成json字符串，由于做了@JSONField注解对应，因此可以直接使用
        String exportE = epService.exportE(jsonString);

        System.out.println("从海关报运系统中获取到的数据：" + exportE);

        /**
         * 将海关返回的json字符串通过fastjson处理成HashMap，因为没有具体的类来做
         * 接收。
         */
        Export returnExport = JSON.parseObject(exportE,Export.class);

        // 根据返回内容更新本地报运单
        Export exportDb = exportService.get(returnExport.getId());
        exportDb.setState(returnExport.getState());
        exportDb.setRemark(returnExport.getRemark());
        exportService.saveOrUpdate(exportDb); //修改数据后保存数据库

        Set<ExportProduct> exportProducts = returnExport.getExportProducts();

        for (ExportProduct ep : exportProducts) {
            // 根据海关返回的货物id查询当前系统的货物对象，进行tax赋值
            ExportProduct epDb = exportProductService.get(ep.getId());
            epDb.setTax(ep.getTax());

            exportProductService.saveOrUpdate(epDb);
        }

        return "alist";
    }

    /**
     * 原始的调用jax_ws风格海关报运webseervice的方法，
     * 由于本地的报运单对象中的属性名称，与海关接口中要求的json字符串中属性名称
     * 不一致，因此不能直接将本地export对象转换成json字符串，传递给接口，
     * 需要构造一个hashMap来进行转换
     * @return
     * @throws Exception_Exception
     */
    @Action(value="exportAction_old_exportE")
    public String old_exportE() throws Exception_Exception {
        // 出口报运单对象
        Export export = exportService.get(this.export.getId());
        /**
         * {
         exportId:"",
         state:"",
         remark:"",
         products:[
         {
         exportProductId:"",
         tax:""
         },
         {
         exportProductId:"",
         tax:""
         }
         ]
         }
         *
         */
        HashMap exportMap = new HashMap();
        exportMap.put("exportId", export.getId());
        // 下面的属性都是为了海关保存mysql数据库时用，并不影响当前系统逻辑
        exportMap.put("boxNums", export.getBoxNums());
        exportMap.put("destinationPort", export.getDestinationPort());
        exportMap.put("customerContract", export.getCustomerContract());

        ArrayList list = new ArrayList();

        Set<ExportProduct> exportProducts = export.getExportProducts();
        for (ExportProduct exportProduct : exportProducts) { //循环报运单中所有的货物封装map数据
            HashMap epMap = new HashMap();
            epMap.put("exportProductId", exportProduct.getId());
            // 下面的属性都是为了海关保存mysql数据库时用，并不影响当前系统逻辑
            epMap.put("cnumber", exportProduct.getCnumber());
            epMap.put("price", exportProduct.getPrice());

            list.add(epMap);
        }

        exportMap.put("products", list);  //封装出口报运货物集合到出口报运map中
        //通过fastjson将对象转成json字符串
        String jsonString = JSON.toJSONString(exportMap);
        //调用海关webservice接口获取海关处理后返回的数据
        /**
         * 这里调用的是通过ilcbs_server_client模块来调用海关jax_ws风格的系统
         * 并返回其处理完毕后的json字符串
         */
        String exportE = epService.exportE(jsonString);

        System.out.println("从海关报运系统中获取到的数据：" + exportE);

        /**
         * 将海关返回的json字符串通过fastjson处理成HashMap，因为没有具体的类来做
         * 接收。
         */
        HashMap returnMap = JSON.parseObject(exportE,HashMap.class);

        // 根据返回内容更新本地报运单
        Export exportDb = exportService.get(returnMap.get("exportId").toString());
        exportDb.setState(Integer.parseInt( returnMap.get("state").toString()));
        exportDb.setRemark(returnMap.get("remark").toString());
        exportService.saveOrUpdate(exportDb); //修改数据后保存数据库

        /**
         * 获取海关返回json字符串中的products列表
         * 通过fastjson的parseArray将products转换成List
         * 注意parseArray的第二个参数是LIST里面的泛型
         * 注意parseArray的第二个参数是LIST里面的泛型
         */
        List<HashMap> returnList = JSON.parseArray(returnMap.get("products").toString(), HashMap.class);

        for (HashMap hashMap : returnList) {
            // 根据海关返回的货物id查询当前系统的货物对象，进行tax赋值
            ExportProduct epDb = exportProductService.get(hashMap.get("exportProductId").toString());
            epDb.setTax(Double.parseDouble( hashMap.get("tax").toString()));

            exportProductService.saveOrUpdate(epDb);
        }

        return "alist";
    }

}
