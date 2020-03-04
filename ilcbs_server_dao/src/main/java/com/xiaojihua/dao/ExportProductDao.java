package com.xiaojihua.dao;

import com.xiaojihua.domain.ExportProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ExportProductDao extends JpaRepository<ExportProduct, String>,JpaSpecificationExecutor<ExportProduct>{


}
