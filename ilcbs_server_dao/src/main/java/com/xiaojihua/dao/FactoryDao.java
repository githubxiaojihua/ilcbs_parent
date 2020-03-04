package com.xiaojihua.dao;

import com.xiaojihua.domain.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface FactoryDao extends JpaRepository<Factory, String>,JpaSpecificationExecutor<Factory>{

}
