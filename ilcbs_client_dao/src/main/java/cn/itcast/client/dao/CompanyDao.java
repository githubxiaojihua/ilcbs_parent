package cn.itcast.client.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.client.domain.CompanyClient;

public interface CompanyDao extends JpaRepository<CompanyClient, String> {

}
