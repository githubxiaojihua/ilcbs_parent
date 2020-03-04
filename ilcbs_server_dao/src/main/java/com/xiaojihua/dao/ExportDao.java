package com.xiaojihua.dao;

import com.xiaojihua.domain.Export;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExportDao extends JpaRepository<Export,String>, JpaSpecificationExecutor<Export> {
}
