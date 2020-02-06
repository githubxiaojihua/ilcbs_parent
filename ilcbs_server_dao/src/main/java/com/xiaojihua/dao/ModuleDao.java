package com.xiaojihua.dao;

import com.xiaojihua.domain.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data JPA userDao
 */
public interface ModuleDao extends JpaRepository<Module,String>,JpaSpecificationExecutor<Module> {
}
