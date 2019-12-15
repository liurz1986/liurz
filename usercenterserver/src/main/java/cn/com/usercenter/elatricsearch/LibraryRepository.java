package cn.com.usercenter.elatricsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends ElasticsearchRepository<Library, Long> {
}
