package cn.itcast.client.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.client.domain.UserClient;

public interface UserClientDao extends JpaRepository<UserClient, String> {
	
	UserClient findByUserNameAndPassword(String userName, String password);

	UserClient findByEmail(String email);
}
