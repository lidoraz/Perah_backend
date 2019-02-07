package com.hnn.dao;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.hnn.Consts;
import com.hnn.dto.Photo;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

public class DatabaseOperations {

    private static final String sqlCreate_USERS = "CREATE TABLE IF NOT EXISTS USERS " +
            "(USER_ID INTEGER PRIMARY KEY NOT NULL," +
            " FNAME      TEXT    NOT NULL, " +
            " LNAME      TEXT    NOT NULL, " +
            " DOB   INTEGER  NOT NULL, " +
            " GENDER    TEXT NOT NULL, " +
            " SEXUAL_ORIENTATION    TEXT NOT NULL, " +
            " RACE    TEXT NOT NULL, " +
            " PROFESSION    TEXT    NOT NULL)";
    private static final String sqlCreate_PHOTOS = "CREATE TABLE IF NOT EXISTS PHOTOS " +
            "(PHOTO_ID TEXT PRIMARY KEY NOT NULL," +
            " PATH      TEXT    NOT NULL, " +
            " GENDER      TEXT    NOT NULL, " +
            " DOB      INTEGER    NOT NULL, " +
            " ORIGIN      TEXT    NOT NULL, " +
            " GLASSES   TEXT NOT NULL)";

    private static final String sqlCreateRatings_RATINGS_ATTRACTIVENESS = "CREATE TABLE IF NOT EXISTS " + Consts.A_TABLE +
            "(RATING_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            " PHOTO_ID  INTEGER NOT NULL," +
            " USER_ID   INTEGER    NOT NULL, " +
            " RATING   INTEGER    NOT NULL, " +
            " RATING_TIME INTEGER NOT NULL, " +
            " SESSION      INTEGER    NOT NULL, " +
            " ITERATION      INTEGER    NOT NULL, " +
            " LOCATION_IN_SESSION      INTEGER    NOT NULL, " +
            " TIME_FOR_RATING      INTEGER    NOT NULL, " +
            " WAS_UNCERTAIN      INTEGER    NOT NULL, " +
            " PHONE_POSITION    TEXT    NOT NULL)";
    private static final String sqlCreateRatings_RATINGS_LOAN_WILLINGNESS = "CREATE TABLE IF NOT EXISTS " + Consts.L_TABLE +
            "(RATING_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            " PHOTO_ID  INTEGER NOT NULL," +
            " USER_ID   INTEGER    NOT NULL, " +
            " RATING   INTEGER    NOT NULL, " +
            " RATING_TIME INTEGER NOT NULL, " +
            " SESSION      INTEGER    NOT NULL, " +
            " ITERATION      INTEGER    NOT NULL, " +
            " LOCATION_IN_SESSION      INTEGER    NOT NULL, " +
            " TIME_FOR_RATING      INTEGER    NOT NULL, " +
            " WAS_UNCERTAIN      INTEGER    NOT NULL, " +
            " PHONE_POSITION    TEXT    NOT NULL)";


    public static boolean isTableExists(String db_Name) {
        File db = new File(db_Name);
        return db.exists();

    }

    public static Connection initTables(String db_Name) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + db_Name); //creates a table if not exists
        if (isTableExists(db_Name)) {
            System.out.println("initTables: creatingTables");
            createTables(conn);
        } else {
            System.out.println("initTables: data base already exists");
        }
        return conn;
    }

    public static void initPhotosTest(Connection conn, String db_Name) throws SQLException, IOException {
        if (isTableExists(db_Name)) {
            fillPhotoDBTest(conn);
        }
    }

    public static void initPhotos(Connection conn, String db_Name) throws SQLException, IOException {
        if (isTableExists(db_Name)) {
            fillPhotoS3(conn);
        }

    }

    private static void fillPhotoS3(Connection conn) throws SQLException, IOException {
        fillPhotoS3(conn, -1);
    }


    /// FILLS PHOTOS FROM ALL DATASET - SCUT and COLORFERET
    private static void fillPhotoS3_mixed(Connection conn, int limit) throws SQLException, IOException {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.EU_WEST_2)
                .build();
        String imagesFolder = "perah_colorferet_image/";
        String metaDataFolder = "perah_colorferet_metadata/";
//        AmazonS3 s3 = new AmazonS3Client(credentials);
        ListObjectsRequest metadataLor = new ListObjectsRequest()
                .withBucketName(Consts.s3Bucket).withPrefix(metaDataFolder);

        ObjectListing objectListing = s3.listObjects(metadataLor);
        String preFix = "https://s3.eu-west-2.amazonaws.com/zumzum-beta/";

//        Statement stmt = conn.createStatement();
        int countLoop = 0;

        for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
            String fileName = summary.getKey();

            if (fileName.endsWith(".txt")) {
                String imgName = fileName.split("/")[1].split(".txt")[0] + ".jpg"; // techincal stuff..
                String imgPATH = preFix + imagesFolder + imgName;
                String gender, dob, origin, glasses;

                URL url = new URL(preFix + fileName); // go over the metadata and extract information
                InputStream is = url.openStream();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    String line = br.readLine(); // read a single line

                    String[] splittedLine = line.split(",");
                    gender = splittedLine[0];
                    dob = splittedLine[1];
                    origin = splittedLine[2];
                    glasses = splittedLine[3];
                }
                String sql = String.format("INSERT INTO PHOTOS VALUES ('%s', '%s', '%s', %s, '%s', '%s');", imgName, imgPATH, gender, dob, origin, glasses);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    countLoop++;
                    if (countLoop % 10 == 0) {
                        System.out.println("insterted images:" + countLoop);
                    }
                    if (countLoop == limit) { //limit amount of images for debug
                        break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /// FILLS PHOTOS FROM COLORFERET
    private static void fillPhotoS3(Connection conn, int limit) throws SQLException, IOException {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.EU_WEST_2)
                .build();
        String imagesFolder = "perah_colorferet_image/";
        String metaDataFolder = "perah_colorferet_metadata/";
//        AmazonS3 s3 = new AmazonS3Client(credentials);
        ListObjectsRequest metadataLor = new ListObjectsRequest()
                .withBucketName(Consts.s3Bucket).withPrefix(metaDataFolder);

        ObjectListing objectListing = s3.listObjects(metadataLor);
        String preFix = "https://s3.eu-west-2.amazonaws.com/zumzum-beta/";

//        Statement stmt = conn.createStatement();
        int countLoop = 0;

        for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
            String fileName = summary.getKey();

            if (fileName.endsWith(".txt")) {
                String imgName = fileName.split("/")[1].split(".txt")[0] + ".jpg"; // techincal stuff..
                String imgPATH = preFix + imagesFolder + imgName;
                String gender, dob, origin, glasses;

                URL url = new URL(preFix + fileName); // go over the metadata and extract information
                InputStream is = url.openStream();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    String line = br.readLine(); // read a single line

                    String[] splittedLine = line.split(",");
                    gender = splittedLine[0];
                    dob = splittedLine[1];
                    origin = splittedLine[2];
                    glasses = splittedLine[3];
                }
                String sql = String.format("INSERT INTO PHOTOS VALUES ('%s', '%s', '%s', %s, '%s', '%s');", imgName, imgPATH, gender, dob, origin, glasses);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    countLoop++;
                    if (countLoop % 10 == 0) {
                        System.out.println("insterted images:" + countLoop);
                    }
                    if (countLoop == limit) { //limit amount of images for debug
                        break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void fillPhotoDBTest(Connection conn) throws SQLException, IOException {
        fillPhotoS3(conn, 10);
//        String PATH = "G:\\My Drive\\Code\\Perah_backend\\src\\main\\resources\\photos\\";
//        File photosDirectory = new File(PATH);
//        Statement stmt = conn.createStatement();
//        for (File f : photosDirectory.listFiles()) {
//            if (f.getName().contains(".jpg")) {
////                System.out.println(f.getName());
//            }
//            if (f.getName().contains(".txt")) {
//                String imgID = f.getName().split(".txt")[0] + ".jpg";
//                String imgPATH = PATH + imgID;
//                String gender, dob, origin, glasses;
//
//                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
//                    String line = br.readLine(); // read a single line
//                    String[] splittedLine = line.split(",");
//                    gender = splittedLine[0];
//                    dob = splittedLine[1];
//                    origin = splittedLine[2];
//                    glasses = splittedLine[3];
//                }
//                String sql = String.format("INSERT INTO PHOTOS VALUES ('%s', '%s', '%s', %s, '%s', '%s');", imgID, imgPATH, gender, dob, origin, glasses);
//                stmt.execute(sql);
//            }
//        }
//        stmt.close();

    }

    public DatabaseOperations() {
    }

    private static void createTables(Connection conn) throws SQLException {
        if (conn == null) {
            throw new IllegalStateException("Must initTables first");
        }
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate_USERS);
        stmt.execute(sqlCreate_PHOTOS);
        stmt.execute(sqlCreateRatings_RATINGS_ATTRACTIVENESS);
        stmt.execute(sqlCreateRatings_RATINGS_LOAN_WILLINGNESS);
        stmt.close();
    }

    public static boolean deleteDB(String dbName) {
        File db = new File(dbName);
        if (db.exists()) {
            if (dbName.equals(Consts.DB)) {
                throw new UnsupportedOperationException("cannot delete '" + Consts.DB + "' ; That is the real database!");
            }
            if (db.delete()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
