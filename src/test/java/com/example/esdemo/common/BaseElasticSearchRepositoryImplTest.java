package com.example.esdemo.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.esdemo.ElasticSearchTestConfig;
import java.io.IOException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@Import({ElasticSearchTestConfig.class})
class BaseElasticSearchRepositoryImplTest {


    @Autowired
    private RestHighLevelClient client;

//    @Autowired
//    private BaseElasticSearchRepository<TestDocument> baseElasticSearchRepository;

    @Test
    @DisplayName("aliasNameWrapper에 indexNameWrapper의 index를 추가하는 AliasAction 생성")
    void updateAliasesTest() {
        // given

        // when

        // then
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
