package com.example.esdemo.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.esdemo.ElasticSearchTestConfig;
import com.example.esdemo.fixture.TestDocument;
import com.example.esdemo.util.IndexUtil;
import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.util.Set;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({SpringExtension.class})
@Import({ElasticSearchTestConfig.class})
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class BaseElasticSearchRepositoryImplTest {

    @Autowired
    private RestHighLevelClient client;

    private BaseElasticSearchRepository<TestDocument> baseElasticSearchRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() throws IOException {
        baseElasticSearchRepository = new BaseElasticSearchRepositoryImpl<>(new ElasticsearchRestTemplate(client));
        client.indices().delete(new DeleteIndexRequest("*"), RequestOptions.DEFAULT);
    }

    @Test
    @DisplayName("index 생성시 설정도 정상적으로 적용된다.")
    void createIndexTest() throws IOException {
        // given
        IndexCoordinates indexNameWrapper = IndexUtil.createIndexNameWrapper("test");
        baseElasticSearchRepository.createIndex(TestDocument.class, indexNameWrapper);

        // when
        GetMappingsResponse response = client.indices().getMapping(new GetMappingsRequest()
                .indices(indexNameWrapper.getIndexName()),
            RequestOptions.DEFAULT);

        // then
        String json = mapper.writeValueAsString(response.mappings().get(indexNameWrapper.getIndexName()).sourceAsMap());

        assertAll(
            () -> assertThat(JsonPath.parse(json).read("$.properties.code.type", String.class)).isEqualTo("keyword"),
            () -> assertThat(JsonPath.parse(json).read("$.properties.standard_name.analyzer", String.class)).isEqualTo("standard")
        );
    }

    @Test
    @DisplayName("인덱스 최초 생성시 alias 가 정상 update 한다.")
    void updateAliasesTest() {
        // given
        IndexCoordinates indexNameWrapper = IndexUtil.createIndexNameWithPostFixWrapper("test");
        baseElasticSearchRepository.createIndex(TestDocument.class, indexNameWrapper);

        baseElasticSearchRepository.save(TestDocument.of(1L, "name1"), indexNameWrapper);

        // when
        IndexCoordinates aliasNameWrapper = IndexUtil.createIndexNameWrapper("test_alias");
        baseElasticSearchRepository.updateAliases(indexNameWrapper, aliasNameWrapper);

        // then
        Set<String> indexNamesByAlias = baseElasticSearchRepository.findIndexNamesByAlias(aliasNameWrapper);
        assertThat(indexNamesByAlias).containsExactly(indexNameWrapper.getIndexName());
    }

    @Test
    @DisplayName("인덱스 updateAliases 시 기존 index의 alias 는 제거된다.")
    void updateAliasesTest2() {
        // given: index 생성 후 alias 설정
        IndexCoordinates indexNameWrapper = IndexUtil.createIndexNameWithPostFixWrapper("test1");
        baseElasticSearchRepository.createIndex(TestDocument.class, indexNameWrapper);

        baseElasticSearchRepository.save(TestDocument.of(1L, "name1"), indexNameWrapper);

        IndexCoordinates aliasNameWrapper = IndexUtil.createIndexNameWrapper("test_alias");
        baseElasticSearchRepository.setAlias(indexNameWrapper, aliasNameWrapper);

        Set<String> indexNamesByAlias = baseElasticSearchRepository.findIndexNamesByAlias(aliasNameWrapper);
        assertThat(indexNamesByAlias).containsExactly(indexNameWrapper.getIndexName());

        // when: 신규 index 생성 후 alias update
        IndexCoordinates newIndexNameWrapper = IndexUtil.createIndexNameWithPostFixWrapper("test2");
        baseElasticSearchRepository.createIndex(TestDocument.class, newIndexNameWrapper);

        baseElasticSearchRepository.save(TestDocument.of(1L, "name1"), newIndexNameWrapper);
        baseElasticSearchRepository.updateAliases(newIndexNameWrapper, aliasNameWrapper);

        // then: 기존 index의 alias는 제거되어 있으며, 신규 index의 alias가 설정되어 있다.
        Set<String> afterUpdateIndexNamesByAlias = baseElasticSearchRepository.findIndexNamesByAlias(aliasNameWrapper);
        assertAll(
            () -> assertThat(afterUpdateIndexNamesByAlias).doesNotContain(indexNameWrapper.getIndexName()),
            () -> assertThat(afterUpdateIndexNamesByAlias).containsExactly(newIndexNameWrapper.getIndexName()),
            () -> assertThat(afterUpdateIndexNamesByAlias).hasSize(1)
        );
    }

    @Test
    @DisplayName("elasticsearch 환경이 격리되어 있어야 한다.")
    void elasticSearchContainerTest() throws IOException {
        boolean isIndexExists = client.indices().exists(new GetIndexRequest("test_index"), RequestOptions.DEFAULT);
        assertThat(isIndexExists).isFalse();

        client.indices().create(new CreateIndexRequest("test_index"), RequestOptions.DEFAULT);

        isIndexExists = client.indices().exists(new GetIndexRequest("test_index"), RequestOptions.DEFAULT);
        assertThat(isIndexExists).isTrue();
    }
}
