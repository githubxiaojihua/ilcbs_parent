package com.xiaojihua.dao;

import com.xiaojihua.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data JPA userDao
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User> {
}
