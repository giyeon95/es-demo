package com.example.esdemo;

import javax.annotation.PreDestroy;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

@TestConfiguration
public class ElasticSearchTestConfig {

    private static final String DOCKER_IMAGE_NAME = "docker.elastic.co/elasticsearch/elasticsearch:7.12.0";

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        ElasticsearchContainer container = esContainer();
        esContainer().start();

        BasicCredentialsProvider credentialProvider = new BasicCredentialsProvider();
        credentialProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials("elasticsearch", "elasticsearch"));

        RestClientBuilder builder = RestClient.builder(HttpHost.create(container.getHttpHostAddress()))
            .setHttpClientConfigCallback(
                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialProvider)
            );

        return new RestHighLevelClient(builder);
    }

    @Bean
    public ElasticsearchContainer esContainer() {
        return new ElasticsearchContainer(DOCKER_IMAGE_NAME);
    }


    @PreDestroy
    public void close() {
        esContainer().stop();
    }
}
