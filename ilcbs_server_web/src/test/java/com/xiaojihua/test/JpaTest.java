package com.xiaojihua.test;

import com.xiaojihua.dao.DeptDao;
import com.xiaojihua.domain.Dept;
import com.xiaojihua.service.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试spring data jpa对dao接口的增强
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:applicationContext.xml")
public class JpaTest {

    //dao没有实现，但是照样可以使用
    @Autowired
    private DeptDao dao;

    @Autowired
    private DeptService service;

    /**
     * 测试使用spring data jpa查询一个dept
     */
    @Test
    public void testFindDept(){
        Dept one = dao.findOne("100");
        System.out.println(one.getDeptName());
    }

    /**
     * 测试新增部门
     */
    @Test
    public void testAddDept(){
        Dept dept = new Dept();
        dept.setDeptName("测试部门");
        dept.setState(1);
        service.saveOrUpdate(dept);
        System.out.println("新增完成");
    }

    /**
     * 测试修改
     */
    @Test
    public void testUpdateDept(){
        Dept dept = new Dept();
        dept.setId("8a8094bb6eb194cc016eb194d14e0000");
        dept.setDeptName("测试2");
        service.saveOrUpdate(dept);

    }

    /**
     * 测试删除
     */
    @Test
    public void testDeleteDept(){
        service.deleteById("8a8094bb6eb194cc016eb194d14e0000");
    }

    /**
     * 使用自定义查询
     */
    @Test
    public void testFindDeptByName(){
        List<Dept> list = dao.findDeptByName("调研组");
        for(Dept d : list){
            System.out.println(d.getDeptName());
        }
    }
}
