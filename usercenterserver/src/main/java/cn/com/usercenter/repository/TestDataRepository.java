package cn.com.usercenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.com.usercenter.entity.TestData;

@Repository
public interface TestDataRepository extends JpaRepository<TestData, String> {

}