package com.hnn;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

public class CredTest {
    public static void main(String[] args){
        System.out.println("Perah Backend");


        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.EU_WEST_2)
                .build();
        for (
                Bucket bucket : s3.listBuckets()) {
            System.out.println(bucket.getName());
        }
    }

}
