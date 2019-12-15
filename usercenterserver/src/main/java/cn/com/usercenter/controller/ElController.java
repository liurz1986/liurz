package cn.com.usercenter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.usercenter.elatricsearch.Library;
import cn.com.usercenter.elatricsearch.LibraryRepository;

@RestController
@RequestMapping("el")
public class ElController {
	@Autowired
	private LibraryRepository libraryRepository;

	@RequestMapping(value = "/seaarch", method = RequestMethod.GET)
	public Map<String, Object> get(String searchValue) {
		QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchValue);
		Iterable<Library> search = libraryRepository.search(builder);
		Iterator<Library> iterator = search.iterator();
		List<Library> librarys = new ArrayList<Library>();
		while (iterator.hasNext()) {
			librarys.add(iterator.next());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", librarys);
		result.put("status", "success");
		return result;
	}

	@RequestMapping(value = "/saves", method = RequestMethod.POST)
	public Map<String, Object> saves(@RequestBody List<Library> librarys) {
		Lock lock = new ReentrantLock();
		lock.tryLock();
		Iterable<Library> search = libraryRepository.saveAll(librarys);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", "success");
		lock.unlock();
		return result;
	}
}
