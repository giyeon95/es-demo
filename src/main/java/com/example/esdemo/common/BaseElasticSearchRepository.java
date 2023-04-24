package com.example.esdemo.common;

import java.util.Set;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseElasticSearchRepository<T> {

    boolean createIndex(Class<T> clazz, IndexCoordinates indexName);

    <S extends T> S save(S entity, IndexCoordinates indexName);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities, IndexCoordinates indexName);

    boolean setAlias(IndexCoordinates indexNameWrapper, IndexCoordinates aliasNameWrapper);

    boolean updateAliases(IndexCoordinates indexNameWrapper, IndexCoordinates aliasNameWrapper);

    Set<String> findIndexNamesByAlias(IndexCoordinates aliasNameWrapper);

    boolean deleteIndex(IndexCoordinates indexNameWrapper);

}
