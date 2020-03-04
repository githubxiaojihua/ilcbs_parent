package com.xiaojihua.web.action.stat;

import com.alibaba.fastjson.JSON;
import com.xiaojihua.service.SqlService;
import com.xiaojihua.service.UserService;
import com.xiaojihua.web.action.BaseAction;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Namespace("/stat")
public class StatChartAction extends BaseAction {

    @Autowired
    private SqlService sqlService;

    @Autowired
    private UserService userService;

    @Action(value="statChartAction_factorysale",results={@Result(name="factorysale",location="/WEB-INF/pages/stat/chart/pieDonut3D.jsp")})
    public String factorySale(){
        return "factorysale";
    }

    @Action(value="statChartAction_getFactorysaleData")
    public String getFactorySaleData() throws IOException {
        String sql = "select t.factory_name,sum(t.amount) from contract_product_c t group by t.factory_name";
        List list = sqlService.executeSQL(sql);
        //[{factoryName: "Czech Republic",amount: 301.90},{factoryName: "Czech Republic",amount: 301.90}]
        List<Map<String,Object>> arr = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            Map<String,Object> map = new HashMap<>();
            map.put("factoryName",list.get(i++));
            map.put("amount",list.get(i));
            arr.add(map);
        }
        String s = JSON.toJSONString(arr);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");

        response.getWriter().write(s);
        return NONE;
    }

    @Action(value="statChartAction_productsale",results={@Result(name="productsale",location="/WEB-INF/pages/stat/chart/column3D.jsp")})
    public String productsale(){
        return "productsale";
    }

    @Action(value="statChartAction_getProductsaleData")
    public String getProductsaleData() throws IOException {
        String sql = "select * from (select t.product_no,sum(t.amount) from contract_product_c t group by t.product_no order by sum(t.amount) desc) where rownum<=15";
        List list = sqlService.executeSQL(sql);
        List<Map<String,Object>> arr = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            Map<String,Object> map = new HashMap<>();
            map.put("productno",list.get(i++));
            map.put("amount",list.get(i));
            map.put("color","#0D8ECF");
            arr.add(map);
        }
        String s = JSON.toJSONString(arr);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(s);
        return NONE;
    }

    @Action(value="statChartAction_onlineinfo",results={@Result(name="onlineInfo",location="/WEB-INF/pages/stat/chart/linSmooth.jsp")})
    public String onlineInfo(){
        return "onlineInfo";
    }

    @Action(value="statChartAction_getOnlineinfoData")
    public String getOnlineInfoData() throws IOException {
        List<Object[]> loginData = userService.getLoginData();
        List<Map<String,Object>> arr = new ArrayList<>();
        for(Object[] obj : loginData){
            Map<String,Object> map = new HashMap<>();
            map.put("hour",obj[0]);
            map.put("value",obj[1]);
            arr.add(map);
        }
        String s = JSON.toJSONString(arr);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.getWriter().write(s);
        return NONE;
    }
}
