package com.hnn;
import com.amazonaws.*;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class UtilityTest {

    public static void main(String[] args){
        AWSCredentials credentials = new BasicAWSCredentials("AKIAJ2IKEQ2K6IYANQQA", "goK4HIdLvK1rR0MG8pbQngZJZgaRdIPtZPNZt7AI");
        AmazonS3 s3 = new AmazonS3Client(credentials);
        ListObjectsRequest lor = new ListObjectsRequest()
                .withBucketName("zumzum-beta");
        ObjectListing objectListing = s3.listObjects(lor);
        String preFix = "https://s3.eu-west-2.amazonaws.com/zumzum-beta/";
        for (S3ObjectSummary summary: objectListing.getObjectSummaries()) {
            String fileName = summary.getKey();
            if(fileName.endsWith(".jpg")){
                String path = preFix + fileName;
                System.out.println(path);
            }

        }
    }
}
