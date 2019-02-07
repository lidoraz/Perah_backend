package com.hnn.dao;

import com.hnn.Consts;
import com.hnn.dto.RatingTypes.ARating;
import com.hnn.dto.RatingTypes.LRating;
import com.hnn.utils.Pair;
import org.apache.tomcat.util.bcel.Const;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

class RatingsTest {

    Connection c = null;
    Ratings ratings;
    @BeforeEach
    void setUp() throws SQLException {
        c = DatabaseOperations.initTables(Consts.TestDB);
        ratings = new Ratings(c);
    }

    @AfterEach
    void tearDown() throws SQLException {
        c.close();
        boolean isDeleted = DatabaseOperations.deleteDB(Consts.TestDB);
        if(!isDeleted){
            throw new IllegalStateException("could not delete");
        }
    }

    @Test
    void getImagesToUser() throws IOException, SQLException {

        DatabaseOperations.initPhotosTest(c,Consts.TestDB);
        //TODO: this is a major function, must be tested.
    }

    @Test
    void insertRatingIntoDB() throws SQLException {
        ARating aRating = new ARating("5","12","5","98765","1","1","12","1245","0","horizontal");
        ratings.insertRatingIntoDB(aRating);
        LRating lRating = new LRating("5","12","5","98765","1","1","12","1245","0","horizontal");
        ratings.insertRatingIntoDB(lRating);
        assert true;
    }
    void insertRatingIntoDB1() throws SQLException {
//        ratings.insertRatingIntoDB(aRating);
        assert true;
    }

    @Test
    void insertARating() {
    }
    @Test
    void getUnfilteredImagesToUserMale(){
        Users usrs = new Users(c);
        try {
            usrs.insertNewUser("12","Smulik","Karras","1993","Male","Female","Black","Student");
            usrs.insertNewUser("13","Anna","Alfi","1994","Female","Male","Asian","Bus Driver");
            DatabaseOperations.initPhotosTest(c,Consts.TestDB);
            HashMap<String, String> photoId_Path_Map = ratings.getUnfilteredImagesToUser("12",Consts.A_RATING);
            assert photoId_Path_Map.size() == 5;
            photoId_Path_Map = ratings.getUnfilteredImagesToUser("12",Consts.L_RATING);
            assert photoId_Path_Map.size() == 10;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    void getUnfilteredImagesToUserFemale(){
        Users usrs = new Users(c);
        try {
            usrs.insertNewUser("12","Smulik","Karras","1993","Male","Female","Black","Student");
            usrs.insertNewUser("13","Anna","Alfi","1994","Female","Male","Asian","Bus Driver");
            DatabaseOperations.initPhotosTest(c,Consts.TestDB);
            HashMap<String, String> photoId_Path_Map = ratings.getUnfilteredImagesToUser("13",Consts.A_RATING);
            assert photoId_Path_Map.size() == 5;
            photoId_Path_Map = ratings.getUnfilteredImagesToUser("13",Consts.L_RATING);
            assert photoId_Path_Map.size() == 10;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    void getCurrentSessionForUser(){
        Users usrs = new Users(c);
        try {
            usrs.insertNewUser("12","Smulik","Karras","1993","Male","Female","Black","Student");
            DatabaseOperations.initPhotosTest(c,"TEST");
            ARating aRating = null;
            aRating = new ARating("5","12","5","98765","1","1","12","1245","0","horizontal");
            ratings.insertRatingIntoDB(aRating);
            aRating = new ARating("5","12","5","98765","2","1","12","1245","0","horizontal");
            ratings.insertRatingIntoDB(aRating);
            LRating lRating = new LRating("6","12","5","98765","14","1","12","1245","0","horizontal");
            ratings.insertRatingIntoDB(lRating);

            assert ratings.getCurrentSessionForUser("12",Consts.A_RATING) == 2;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void getCurrentIterationForUser(){
        Users usrs = new Users(c);
        try {
            usrs.insertNewUser("12","Smulik","Karras","1993","Male","Female","Black","Student");
            DatabaseOperations.initPhotosTest(c,Consts.TestDB);
            ARating aRating = null;
            aRating = new ARating("5","12","5","98765","1","1","12","1245","0","horizontal");
            ratings.insertRatingIntoDB(aRating);
            aRating = new ARating("5","12","5","98765","2","0","12","1245","0","horizontal");
            ratings.insertRatingIntoDB(aRating);
            LRating lRating = new LRating("6","12","5","98765","14","1","12","1245","0","horizontal");
            ratings.insertRatingIntoDB(lRating);

            assert ratings.getCurrentIterationForUser("12",Consts.A_RATING) == 1;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void filterRatedImages(){
        Users usrs = new Users(c);
        try {
            String userID = "12";
            String sessType = Consts.A_RATING;
            String photoID1 = "CF590.jpg";
            String photoID2 = "CF591.jpg";
            usrs.insertNewUser(userID,"Smulik","Karras","1993","Male","Female","Black","Student");
            DatabaseOperations.initPhotosTest(c,Consts.TestDB);

            ARating aRating1 = new ARating(photoID1, userID,"5","98765","1","1","12","1245","0","horizontal");
            ARating aRating2 = new ARating(photoID2, userID,"5","98765","1","1","12","1245","0","horizontal");

            String [] remainingPhotos = ratings.getImagesToUser(userID, sessType).getKey();
            assert remainingPhotos.length == 5;
            ratings.insertRatingIntoDB(aRating1);
            ratings.insertRatingIntoDB(aRating2);
            assert ratings.getNumberOfRatings(userID, sessType) == 2;
            remainingPhotos = ratings.getImagesToUser(userID, sessType).getKey();
            assert remainingPhotos.length == 3;

            assert ratings.getCurrentIterationForUser("12",Consts.A_RATING) == 1;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void addRatingsToNextIteration() throws InterruptedException {
        Users usrs = new Users(c);
        try {
            String userID = "12";
            String sessType = Consts.A_RATING;
            String photoID0 = "CF588.jpg";
            String photoID1 = "CF589.jpg";
            String photoID2 = "CF590.jpg";
            String photoID3 = "CF591.jpg";
            String photoID4 = "CF592.jpg";
            usrs.insertNewUser(userID,"Smulik","Karras","1993","Male","Female","Black","Student");
            DatabaseOperations.initPhotosTest(c,Consts.TestDB);

            ARating aRating0 = new ARating(photoID0, userID,"5","98765","1","1","12","1245","0","horizontal");
            ARating aRating1 = new ARating(photoID1, userID,"5","98765","1","1","12","1245","0","horizontal");
            ARating aRating2 = new ARating(photoID2, userID,"5","98765","1","1","12","1245","0","horizontal");
            ARating aRating3 = new ARating(photoID3, userID,"5","98765","1","1","12","1245","0","horizontal");
            ARating aRating4 = new ARating(photoID4, userID,"5","98765","1","1","12","1245","0","horizontal");

            // session is updated from SessionController, because the flag from getImages has set to true.
            ARating aRating1_1 = new ARating(photoID1, userID,"5","98765",
                    "2","2","12","1245","0","horizontal");


            String [] remainingPhotos = ratings.getImagesToUser(userID, sessType).getKey();
            assert remainingPhotos.length == 5;
            ratings.insertRatingIntoDB(aRating0);
            ratings.insertRatingIntoDB(aRating1);
            ratings.insertRatingIntoDB(aRating2);
            ratings.insertRatingIntoDB(aRating3);
            ratings.insertRatingIntoDB(aRating4);

            assert ratings.getNumberOfRatings(userID, sessType) == 5;
            Thread.sleep(1000);
            Pair imagesAndBolean = ratings.getImagesToUser(userID, sessType);
            remainingPhotos = imagesAndBolean.getKey();
            boolean increaseIter = imagesAndBolean.getValue();

            assert remainingPhotos.length == 5;
            assert increaseIter; // must increase iteration
            assert ratings.getCurrentIterationForUser("12",Consts.A_RATING) == 1;
            ratings.insertRatingIntoDB(aRating1_1);

            assert ratings.getNumberOfRatings(userID, sessType) == 6;
            imagesAndBolean = ratings.getImagesToUser(userID, sessType);
            remainingPhotos = imagesAndBolean.getKey();
            increaseIter = imagesAndBolean.getValue();
            assert remainingPhotos.length == 4;
            assert !increaseIter;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void randomImages() {
        String[] justStrs = new String[100];

        for (int i = 0; i < justStrs.length; i++) {
            justStrs[i] = "img" + i + ".jpg";
        }
        String[] randomStrings1 = Ratings.getRandomizedImagePaths(justStrs, 20);
        String[] randomStrings2 = Ratings.getRandomizedImagePaths(justStrs, 42);
        HashSet<String> strs1 = new HashSet<>(Arrays.asList(randomStrings1));
        HashSet<String> strs2 = new HashSet<>(Arrays.asList(randomStrings2));
        int differentSet = 0;
        for (String s : strs1) {
            if (!strs2.contains(s)) {
                differentSet++;
            }
        }
        for (String s : strs2) {
            if (!strs1.contains(s)) {
                differentSet++;
            }
        }
//        System.out.println("different strings between runs: " + differentSet);// 32
        assert differentSet == 32;
    }
}