package com.example.esdemo.domain;

import com.example.esdemo.common.BaseElasticSearchRepository;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocumentRepository extends ElasticsearchRepository<UserDocument, Long>,
    BaseElasticSearchRepository<UserDocument> {

//    List<UserDocument> findUserDocumentsByName(String name);
}
