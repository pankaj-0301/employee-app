package com.employwise.employee.config;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

@Configuration
public class CouchDbConfig {
    
    @Value("${couchdb.url}")
    private String couchDbUrl;
    
    @Value("${couchdb.username}")
    private String couchDbUsername;
    
    @Value("${couchdb.password}")
    private String couchDbPassword;
    
    @Value("${couchdb.database}")
    private String couchDbDatabase;
    
    @Bean
    public HttpClient httpClient() {
        try {
            return new StdHttpClient.Builder()
                    .url(couchDbUrl)
                    .username(couchDbUsername)
                    .password(couchDbPassword)
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid CouchDB URL: " + couchDbUrl, e);
        }
    }
    
    @Bean
    public CouchDbInstance couchDbInstance() {
        return new StdCouchDbInstance(httpClient());
    }
    
    @Bean
    public CouchDbConnector couchDbConnector() {
        CouchDbConnector connector = new StdCouchDbConnector(couchDbDatabase, couchDbInstance());
        // Create database if it doesn't exist
        connector.createDatabaseIfNotExists();
        return connector;
    }
}