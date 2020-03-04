package com.xiaojihua.dao;

import com.xiaojihua.domain.ExtCproduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ExtCproductDao extends JpaRepository<ExtCproduct, String>,JpaSpecificationExecutor<ExtCproduct>{

	
}
