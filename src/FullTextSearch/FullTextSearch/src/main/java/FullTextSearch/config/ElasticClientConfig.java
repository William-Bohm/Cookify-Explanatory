package com.Cookify.ElasticSearch.config;

import com.Cookify.security.services.Impl.RecipeServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@EnableElasticsearchRepositories(basePackages = "com.Cookify.ElasticSearch.repository")
@ComponentScan(basePackages = { "com.Cookify.ElasticSearch" })
public class ElasticClientConfig extends ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.rest.host}")
    private String hostName;

    @Value("${spring.elasticsearch.rest.port}")
    private int port;

    @Value("${spring.elasticsearch.rest.password}")
    private String password;

    @Value("${spring.elasticsearch.rest.username}")
    private String username;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(hostName + ":" + port)
                .withBasicAuth(username, password)
                .withConnectTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }
}