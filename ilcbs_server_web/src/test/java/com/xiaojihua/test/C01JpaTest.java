package com.xiaojihua.test;

import com.xiaojihua.dao.DeptDao;
import com.xiaojihua.domain.Dept;
import com.xiaojihua.service.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试spring data jpa对dao接口的增强
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:applicationContext.xml")
public class C01JpaTest {

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
        dept.setDeptName("测试23");
        service.saveOrUpdate(dept);

    }

    /**
     * 测试删除
     */
    @Test
    public void testDeleteDept(){
        service.deleteById("8affff816fc26926016fc26aabf00000");
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

    /**
     * 测试命名规范的等值查询
     */
    @Test
    public void testNamedQuery(){
        List<Dept> deptList = dao.findDeptByState(1);
        for(Dept t : deptList){
            System.out.println(t.getDeptName());
        }
    }

    /**
     * 测试命名规范的模糊查询
     * 具体的命名规范关键字可以参考：Spring Data JPA支持多种查询.docx
     */
    @Test
    public void testNamedLikeQuery(){
        List<Dept> deptList = dao.findDeptByDeptNameLike("%部");
        for(Dept t : deptList){
            System.out.println(t.getDeptName());
        }
    }

    /**
     * 原生SQL语句
     */
    @Test
    public void testNativeSqlQuery(){
        List<Dept> deptList = dao.findNameAndState("总裁办", 1);
        for(Dept t : deptList){
            System.out.println(t.getDeptName());
        }
    }

    /**
     * 使用spring data jpa 内置的findall方法配合pageable进行分页
     * 此处使用的是pageable的实现类PageRequest
     *
     * 另外使用了排序查询
     *
     * pageable可以应用于内置查询，自定义查询等，当有多个参数的时候，其必须为最后一个参数
     */
    @Test
    public void testPageQuery(){
        //排序查询
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Page<Dept> page = dao.findAll(new PageRequest(0, 5,sort));
        System.out.println("总记录数：" + page.getTotalElements());
        System.out.println("总页数：" + page.getTotalPages());
        List<Dept> content = page.getContent();
        for(Dept t : content){
            System.out.println(t.getId() + "------" + t.getDeptName());
        }

    }

    /**
     * 限制查询，只查询第一条记录，也可以应用于排序
     */
    @Test
    public void testRestictQuery(){
        List<Dept> deptList = dao.findFirstByDeptNameLike("%部门");
        for(Dept t : deptList){
            System.out.println(t.getId() + "------" + t.getDeptName());
        }
    }
}
