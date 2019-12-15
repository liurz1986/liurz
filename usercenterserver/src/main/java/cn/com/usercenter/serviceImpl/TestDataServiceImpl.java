package cn.com.usercenter.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.usercenter.entity.TestData;
import cn.com.usercenter.repository.TestDataRepository;
import cn.com.usercenter.service.TestDataService;

@Service
@Transactional
public class TestDataServiceImpl implements TestDataService {

	private Logger log = LoggerFactory.getLogger(TestDataServiceImpl.class);
	@Autowired
	private TestDataRepository testDataRepository;
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Override
	public void save(TestData data) {

		testDataRepository.save(data);
		userServiceImpl.test();

	}

}
