package com.conner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "cognito.client")
@Data
public class CogntioConfig {
    private String key;
    private String secret;
    private String region;
    private String poolid;

    public AWSCredentials getCredentials() {

        System.out.println("KEY:" + key);
        System.out.println("PoolId:" + poolid);
        return new BasicAWSCredentials(key, secret);
    }

    public AWSCredentialsProvider gCredentialsProvider() {

        return new AWSStaticCredentialsProvider(getCredentials());
    }
}
