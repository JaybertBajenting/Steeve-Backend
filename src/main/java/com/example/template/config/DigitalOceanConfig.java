package com.example.template.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DigitalOceanConfig {





    @Value("${spring.digitalocean.accessKey}")
    private String accessKey;

    @Value("${spring.digitalocean.secretKey}")
    private String secretKey;

    @Value("${spring.digitalocean.region}")
    private String region;


    @Bean
    public AmazonS3 amazonS3(){

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(region,"spg1"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withPathStyleAccessEnabled(true)
                .build();
    }


}
