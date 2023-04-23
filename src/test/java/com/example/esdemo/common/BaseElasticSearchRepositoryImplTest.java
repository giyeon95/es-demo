package com.example.esdemo.common;

import com.example.esdemo.fixture.TestDocument;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.assertj.core.api.Assertions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

@SpringBootTest
@RequiredArgsConstructor
class BaseElasticSearchRepositoryImplTest {

    @Autowired
    private BaseElasticSearchRepository<TestDocument> baseElasticSearchRepository;

    @Test
    @DisplayName("aliasNameWrapper에 indexNameWrapper의 index를 추가하는 AliasAction 생성")
    void updateAliasesTest() {
        // given

        // when

        // then
    }

    @Test
    public void test() throws IOException {
        ElasticsearchContainer container
            = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.12.0");
        container.start();

        BasicCredentialsProvider credentialProvider = new BasicCredentialsProvider();
        credentialProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials("elasticsearch", "elasticsearch"));

        RestClientBuilder builder = RestClient.builder(HttpHost.create(container.getHttpHostAddress()))
            .setHttpClientConfigCallback(
                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialProvider)
            );

        RestHighLevelClient client = new RestHighLevelClient(builder);
        // index생성전 index가 존재하는지 확인
        boolean isIndexExists = client.indices().exists(new GetIndexRequest("test_index"), RequestOptions.DEFAULT);
        Assertions.assertThat(isIndexExists).isEqualTo(false);

        client.indices().create(new CreateIndexRequest("test_index"), RequestOptions.DEFAULT);

        // index생성후 index가 존재하는지 확인
        isIndexExists = client.indices().exists(new GetIndexRequest("test_index"), RequestOptions.DEFAULT);
        Assertions.assertThat(isIndexExists).isEqualTo(true);
    }
}
