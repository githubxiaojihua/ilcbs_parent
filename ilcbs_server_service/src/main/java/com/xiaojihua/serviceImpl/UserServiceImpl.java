package com.xiaojihua.serviceImpl;

import com.xiaojihua.dao.DeptDao;
import com.xiaojihua.dao.UserDao;
import com.xiaojihua.domain.Dept;
import com.xiaojihua.domain.User;
import com.xiaojihua.service.DeptService;
import com.xiaojihua.service.UserService;
import com.xiaojihua.utils.UtilFuns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao dao;

    @Override
    public List<User> find(Specification<User> spec) {
        return dao.findAll(spec);
    }

    @Override
    public User get(String id) {
        //find相当于get没有延时加载的问题
        return dao.findOne(id);
    }

    @Override
    public Page<User> findPage(Specification<User> spec, Pageable pageable) {
        //从这里也看出应该在dao中继承JpaSpecificationExecutor
        return dao.findAll(spec,pageable);
    }

    /**
     * 更新和插入都走dao.save方法。
     * 根据entity的id来判断是否是新增，如果id是新的那么就是新增，
     * 如果id已经在数据库中了那么就是修改。
     * @param entity
     */
    @Override
    public void saveOrUpdate(User entity) {
        if(UtilFuns.isEmpty(entity.getId())){
            String id = UUID.randomUUID().toString();
            entity.setId(id);
            entity.getUserinfo().setId(id);
        }else{

        }
        dao.save(entity);
    }

    @Override
    public void saveOrUpdateAll(Collection<User> entitys) {
        dao.save(entitys);
    }

    @Override
    public void deleteById(String id) {
        dao.delete(id);
    }

    @Override
    public void delete(String[] ids) {
        for(String id : ids){
            dao.delete(id);
        }
    }
}
