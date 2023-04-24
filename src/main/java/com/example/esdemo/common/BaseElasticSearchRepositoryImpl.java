package com.example.esdemo.common;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.index.AliasAction.Add;
import org.springframework.data.elasticsearch.core.index.AliasAction.Remove;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BaseElasticSearchRepositoryImpl<T> implements BaseElasticSearchRepository<T> {

    private final ElasticsearchOperations operations;

    @Override
    public boolean createIndex(Class<T> clazz, IndexCoordinates indexName) {
        IndexOperations indexOperations = operations.indexOps(clazz);

        // copy clazz settings and mappings and setting indexName
        return operations.indexOps(indexName).create(indexOperations.createSettings(), indexOperations.createMapping());
    }

    @Override
    public <S extends T> S save(S entity, IndexCoordinates indexName) {
        if (!operations.indexOps(indexName).exists()) { // "settings.mapper.dynamic": false 시에는 해당로직 제거 가능
            throw new IllegalStateException("Index not exist: " + indexName.getIndexName());
        }

        return operations.save(entity, indexName);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities, IndexCoordinates indexName) {
        if (!operations.indexOps(indexName).exists()) { // "settings.mapper.dynamic": false 시에는 해당로직 제거 가능
            throw new IllegalStateException("Index not exist: " + indexName.getIndexName());
        }

        return operations.save(entities, indexName);
    }

    @Override
    public boolean setAlias(IndexCoordinates indexNameWrapper, IndexCoordinates aliasNameWrapper) {
        IndexOperations indexOperations = operations.indexOps(indexNameWrapper);
        AliasActions aliasActions = new AliasActions();
        aliasActions.add(aliasAddAction(aliasNameWrapper, indexOperations.getIndexCoordinates()));

        return indexOperations.alias(aliasActions);
    }

    @Override
    public boolean updateAliases(IndexCoordinates indexNameWrapper, IndexCoordinates aliasNameWrapper) {
        Set<String> existIndexNames = findIndexNamesByAlias(aliasNameWrapper);
        Remove remove = aliasRemoveAction(aliasNameWrapper, existIndexNames);

        IndexOperations indexOperations = operations.indexOps(indexNameWrapper);
        Add add = aliasAddAction(aliasNameWrapper, indexOperations.getIndexCoordinates());

        AliasActions aliasActions = new AliasActions(remove, add);
        return indexOperations.alias(aliasActions);
    }

    /**
     * aliasNameWrapper에 indexNameWrapper의 index를 추가하는 AliasAction 생성
     *
     * @param aliasNameWrapper
     * @param indexCoordinates
     * @return
     */
    private Add aliasAddAction(IndexCoordinates aliasNameWrapper, IndexCoordinates indexCoordinates) {
        return new Add(AliasActionParameters.builder()
            .withIndices(indexCoordinates.getIndexNames())
            .withAliases(aliasNameWrapper.getIndexName())
            .build());
    }

    /**
     * aliasNameWrapper에 존재하는 index를 제거하는 AliasAction 생성
     *
     * @param aliasNameWrapper
     * @param existIndexNames
     * @return
     */
    private Remove aliasRemoveAction(IndexCoordinates aliasNameWrapper, Set<String> existIndexNames) {
        if (existIndexNames.isEmpty()) {
            return null;
        }

        return new Remove(AliasActionParameters.builder()
            .withIndices(existIndexNames.toArray(String[]::new))
            .withAliases(aliasNameWrapper.getIndexName())
            .build());
    }

    @Override
    public Set<String> findIndexNamesByAlias(IndexCoordinates aliasNameWrapper) {
        IndexOperations indexOperations = operations.indexOps(aliasNameWrapper);
        return indexOperations.getAliasesForIndex(aliasNameWrapper.getIndexName()).keySet();
    }

    @Override
    public boolean deleteIndex(IndexCoordinates indexNameWrapper) {
        IndexOperations indexOperations = operations.indexOps(indexNameWrapper);
        return indexOperations.delete();
    }

}
