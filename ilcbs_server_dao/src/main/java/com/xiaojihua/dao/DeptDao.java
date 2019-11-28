package com.xiaojihua.dao;

import com.xiaojihua.domain.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 使用spring data jpa 需要在接口上继承两个接口
 * jparepository只有继承了才能被增强，Dept指操作的实体类，String指的是实体类的ID类型
 * jparepository有一个实现类SimpleJpaRepository，里面使用的还是EntityManager，真正
 * 查询的时候还是走到了SimpleJpaRepository中。
 *
 *
 * jpaspecificationexecutor只有继承了才能进行条件查询，不继承的话dao就会缺少
 * 条件查询的方法 findOne(Specification<T> var1)等有方法
 *
 * 注意这里不需要写@Repository等spring注解。原来没有使用spring data jpa的时候也是不是在
 * 接口中加注解，是在实现类中加。spring data jpa之所以能只提供接口就能实现功能，我的理解是：
 * 当在service使用@Autoware来注解DeptDao的时候，因为spring 注入的时候看有没有接口的实现类
 * 一看有SimpleJpaRepository因此就注入了，而SimpleJpaRepository里面就有了相关的方法和增强，
 * 这里满足装饰者模式（有统一的接口）
 *
 * 以后可以不用写实现类了
 */
public interface DeptDao extends JpaRepository<Dept,String>,JpaSpecificationExecutor<Dept> {

    /**
     * 自定义方法，单纯从JpaRepository只能使用里面即有的方法，要扩展就要使用自定义方法
     * @param name
     * @return
     */
    @Query("from Dept where deptName=?1")//jpql
    public List<Dept> findDeptByName(String name);
}
