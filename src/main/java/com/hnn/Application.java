package com.hnn;
//package hello;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.UploadObjectRequest;
import com.hnn.dao.DatabaseOperations;
import com.hnn.dao.Ratings;
import com.hnn.dao.Users;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import static com.hnn.Consts.s3Bucket;


@SpringBootApplication
public class Application {

    public static Connection c = null;
    public static Users users = null;
    public static Ratings ratings = null;
    public static final long runID = System.currentTimeMillis();
    public static void main(String[] args) throws SQLException, IOException {

        System.out.println("Perah Backend");

        c = DatabaseOperations.initTables(Consts.TestDB);
        if(args.length ==0){
            System.out.println("add arg 'initP' to init photos from amazon s3");
        }
        else{
            if(args[0].equals("initP")){
                System.out.println("init photos from Amazon S3");
                DatabaseOperations.initPhotos(c, Consts.TestDB);
            }
            if(args[0].equals("initPTest")){
                System.out.println("init photos from Local Images");
                DatabaseOperations.initPhotosTest(c, Consts.TestDB);
            }
        }

        String dbName = Consts.TestDB;
        Timer timer = new Timer();
        long oneHour = 3600000;
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.EU_WEST_2)
                .build();
        for (Bucket bucket :s3.listBuckets()){
            System.out.println(bucket.getName());
        }

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                s3.putObject(s3Bucket,  runID +"_"+ dbName , new File(dbName));
                System.out.println("Put an object in S3:" + dbName);
            }
        },0,oneHour);

        users = new Users(c);
        ratings = new Ratings(c);

        SpringApplication.run(Application.class, args);
    }

}