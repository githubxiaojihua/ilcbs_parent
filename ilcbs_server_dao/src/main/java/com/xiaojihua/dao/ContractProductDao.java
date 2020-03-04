package com.xiaojihua.dao;

import com.xiaojihua.domain.Contract;
import com.xiaojihua.domain.ContractProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring data JPA ContractProductsDao
 */
public interface ContractProductDao extends JpaRepository<ContractProducts,String>,JpaSpecificationExecutor<ContractProducts> {
    @Query(value="from ContractProducts where to_char(contract.shipTime,'yyyy-MM') = ?1")
    public List<ContractProducts> findCpByShipTime(String shipTime);

    /**
     * jpql中in可以接收数组作为参数
     * 但是如果是hql则不行
     *
     * 注意这里有个问题：当in后面没有空格直接跟(的时候，运行时会报错：
     * Encountered array-valued parameter binding, but was expecting [java.lang.String (n/a)];
     * 但是如果加上个空格就没有问题了，
     * 所以使用jpql书写sql语句的时候要规范，该有空格就有空格
     * @param ids
     * @return
     */
    @Query(value="from ContractProducts where contract.id in (?1)")
    public List<ContractProducts> findCpByContractIds(String[] ids);
}
