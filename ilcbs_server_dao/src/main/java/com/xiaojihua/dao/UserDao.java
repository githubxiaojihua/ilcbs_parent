package com.xiaojihua.dao;

import com.xiaojihua.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring data JPA userDao
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User> {

    /**
     * 原生SQL查询系统访问量数据
     * @return
     */
    @Query(value="select a1,nvl(p_count,0) from (select to_char(login_time,'HH24') p_time,count(*) p_count from login_log_p group by to_char(login_time,'HH24')) p,online_info_t where p.p_time(+) = online_info_t.a1 order by a1",nativeQuery=true)
    public List<Object[]> getLoginData();
}
