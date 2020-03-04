package com.xiaojihua.dao;

import com.xiaojihua.domain.Contract;
import com.xiaojihua.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data JPA ContractDao
 */
public interface ContractDao extends JpaRepository<Contract,String>,JpaSpecificationExecutor<Contract> {
}
