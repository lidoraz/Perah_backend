package com.hnn.dao;

import com.hnn.*;
import com.hnn.dto.RatingTypes.Rating;
import com.hnn.utils.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Ratings extends Dao {

    // fetch user number of iterations
    // fetch user photos for session
    // insert ratings
    //
    private static String getOppositeGender(String userGender) {
        if (userGender.equals("Male")) {
            return "Female";
        } else {
            if (userGender.equals("Female")) {
                return "Male";
            } else {
                throw new UnsupportedOperationException("only 'Male' / 'Female' supported");
            }
        }
    }

    public Ratings(Connection c) {
        super(c);
    }

    /**
     * photoId -> path mapping
     * Can improve this algorithm because It is not needed to get all the items when we start new iteration. //TODO
     *
     * @param userID
     * @param type   will return full images depending on type, A will return only opposite gender, L would return all photos.
     * @return
     * @throws SQLException
     */
    public HashMap<String, String> getUnfilteredImagesToUser(String userID, String type) throws SQLException {
        HashMap<String, String> photoId_Path_Map = new HashMap<>();
        ResultSet rs;
        Statement stmt = c.createStatement();
        if (type.equals(Consts.L_RATING)) { //loan
            String sqlGetALLPhotos = "SELECT * FROM PHOTOS";
            rs = stmt.executeQuery(sqlGetALLPhotos);
        } else {
            if (type.equals(Consts.A_RATING)) { //ATTRACTIVENESS
                String userGender = Users.getUserGender(userID, c);
                String reqGender = getOppositeGender(userGender);
                String sqlGetGenderPhotos = String.format("SELECT PHOTO_ID, PATH FROM PHOTOS WHERE GENDER = '%s';", reqGender);
                rs = stmt.executeQuery(sqlGetGenderPhotos);
            } else {
                throw new UnsupportedOperationException("type is not valid");
            }
        }
        String photoID;
        String path;
        while (rs.next()) {
            photoID = rs.getString("PHOTO_ID");
            path = rs.getString("PATH");
            photoId_Path_Map.put(photoID, path);
        }
        stmt.close();
        return photoId_Path_Map;
    }

    /**
     * For a userID, and type of task,
     * Sends an array of paths to images which have not been rated yet
     * If user has rated all the photos in current iteration, it will return a flag for increasing the iteration
     * the flag is handled in SessionController
     * @param userID
     * @param type
     * @return pair of array of paths to images , boolean if needs to increment iteration
     * @throws SQLException
     */
    public Pair getImagesToUser(String userID, String type) throws SQLException {
        boolean increaseIteration = false;
        // select images which has not been rated yet
        HashMap<String, String> photoId_Path_Map = getUnfilteredImagesToUser(userID, type); // get all images id's, subtract what the user already seen.
        HashSet<String> photoIDsMaxIter = getRatedImagesWithMaxIterationByUser(userID, type); // get current iteration: select the max value of iteration images where user id = id.
        if (photoIDsMaxIter != null) {
            for (String photoID : photoIDsMaxIter) { // perform SELECT IMAGES where userID equals, only from current iteration.
                photoId_Path_Map.remove(photoID);
            }
        }
        String[] remainingImagesPaths = photoId_Path_Map.values().toArray(new String[0]); //

        if (remainingImagesPaths.length == 0) {
            //increment counter for user. could send the value as json, something like that.... package the image path with the iteration number.
            photoId_Path_Map = getUnfilteredImagesToUser(userID, type);
            remainingImagesPaths = photoId_Path_Map.values().toArray(new String[0]);
            increaseIteration = true;
        }
        String[] toUserImagesPath = getRandomizedImagePaths(remainingImagesPaths);


        return new Pair(toUserImagesPath, increaseIteration);
    }

    private static void exchangeStrings(String[] arr, int from, int to) {
        String temp = arr[to];
        arr[to] = arr[from];
        arr[from] = temp;
    }
    private static String[] getRandomizedImagePaths(String[] remainingImagesPaths){
        return getRandomizedImagePaths(remainingImagesPaths, -1);
    }
    public static String[] getRandomizedImagePaths(String[] remainingImagesPaths, int seed){
        Random r;
        if(seed == -1){
            r = new Random();
        }
        else{
            r = new Random(seed);
        }

        String[] imagesPath = new String[Math.min(Consts.NUM_IMAGES_PER_SESSIONS, remainingImagesPaths.length)];
        // get random number from entries, move it to first location, and random from the 2nd location.
        for (int i = 0; i < imagesPath.length; i++) {
            int idxToTake = r.nextInt(remainingImagesPaths.length - i) + i;
            imagesPath[i] = remainingImagesPaths[idxToTake];
            exchangeStrings(remainingImagesPaths, idxToTake, i); //exchange:
        }
        return imagesPath;
    }

    public HashSet<String> getRatedImagesWithMaxIterationByUser(String userID, String type) throws SQLException {
        HashSet<String> photosIDs = new HashSet<>();
        if (getNumberOfRatings(userID, type) == 0) {
            return null;
        }

        int maxIterationForUser = getCurrentIterationForUser(userID, type);
        String sqlPhotoIDWhereUserAndMaxIteration = String.format("SELECT PHOTO_ID FROM %s WHERE USER_ID=%s AND ITERATION=%d", getTableFromType(type), userID, maxIterationForUser);
//        String sqlPhotoIDWhereUserAndMaxIteration = String.format("SELECT PHOTO_ID FROM %s WHERE USER_ID = %s AND MAX(ITERATION)", getTableFromType(type), userID);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sqlPhotoIDWhereUserAndMaxIteration);
        while (rs.next()) {
            photosIDs.add(rs.getString("PHOTO_ID"));
        }
        return photosIDs;
    }

    public Integer getNumberOfRatings(String userID, String type) throws SQLException {
        Statement stmt = c.createStatement();
        String sqlNumRatings = String.format("SELECT count(PHOTO_ID) as NUM_PHOTOS FROM %s WHERE USER_ID = %s", getTableFromType(type), userID);
        ResultSet rs = stmt.executeQuery(sqlNumRatings);
        while (rs.next()) {
            return Integer.valueOf(rs.getString("NUM_PHOTOS"));
        }
        throw new IllegalStateException("not valid state - tables not init");
    }

    private String getTableFromType(String type) {
        if (type.equals(Consts.A_RATING)) {
            return Consts.A_TABLE;
        }
        if (type.equals(Consts.L_RATING)) {
            return Consts.L_TABLE;
        }
        throw new IllegalStateException("not valid String type");
    }

    // Abstract method for iteration and session
    private Integer getCurrentParameterForUser(String userID, String type, String param) throws SQLException {
        Statement stmt = c.createStatement();
        String sqlCurrentIteration = String.format("SELECT MAX(%s) as %s FROM %s WHERE USER_ID = %s", param, param, getTableFromType(type), userID);
        ResultSet rs = stmt.executeQuery(sqlCurrentIteration);
        Integer paramValue = 0;
        while (rs.next()) {
            String rss = rs.getString(param);
            if (rss != null) {
                paramValue = Integer.valueOf(rss);
            }
        }
        stmt.close();
        return paramValue;
    }

    public Integer getCurrentIterationForUser(String userID, String type) throws SQLException {
        return getCurrentParameterForUser(userID, type, "ITERATION");

    }

    // use this to implement session increment, every time user connects, session will be updated.
    public Integer getCurrentSessionForUser(String userID, String type) throws SQLException {
        return getCurrentParameterForUser(userID, type, "SESSION");
    }

    public void insertRatingIntoDB(Rating r) throws SQLException {
        String ratingType = r.getRatingType();
        Statement stmt = c.createStatement();
        String sqlPostfix = String.format("(" +
                        "PHOTO_ID," +
                        "USER_ID," +
                        "RATING," +
                        "RATING_TIME," +
                        "SESSION," +
                        "ITERATION," +
                        "LOCATION_IN_SESSION," +
                        "TIME_FOR_RATING," +
                        "WAS_UNCERTAIN," +
                        "PHONE_POSITION) VALUES " +
                        "('%s','%s',%s,%s,%s,%s,%s,%s,%s,'%s');",
                r.getPhotoId(),
                r.getUserId(),
                r.getRatingValue(),
                r.getRatingUUID(),
                r.getSession(),
                r.getIteration(),
                r.getLocationInSession(),
                r.getTimeTook(),
                r.getTimesUncertain(),
                r.getPhonePosition());

        if (ratingType.equals(Consts.A_RATING)) { //ATTRACTIVENESS
            insertARating(sqlPostfix, stmt);
        } else {
            if (ratingType.equals(Consts.L_RATING)) {  //Willing for a loan
                insertLRating(sqlPostfix, stmt);
            } else {
                throw new UnsupportedOperationException("unknown ratingType");
            }
        }
        stmt.close();
    }

    private void insertLRating(String sqlPostfix, Statement stmt) throws SQLException {
        String sql = "INSERT INTO RATINGS_LOAN_WILLINGNESS" + sqlPostfix;
        stmt.execute(sql);
    }

    private void insertARating(String sqlPostfix, Statement stmt) throws SQLException {
        String sql = "INSERT INTO RATINGS_ATTRACTIVENESS" + sqlPostfix;
        stmt.execute(sql);
    }

    public int getTotalRatings(String type) throws SQLException {
        if (!type.equals(Consts.A_RATING) & !type.equals(Consts.L_RATING)) {
            return -1;
        }
        Statement stmt = c.createStatement();
        String sql = "SELECT COUNT(RATING_ID) AS RATINGS FROM " + getTableFromType(type);
        ResultSet rs = stmt.executeQuery(sql);
        int numRatings = 0;
        while (rs.next()) {
            numRatings = rs.getInt("RATINGS");
        }
        stmt.close();
        return numRatings;
    }
    public Map<String, Integer> getTotalRatingsAllUsers(String type) throws SQLException {
        if (!type.equals(Consts.A_RATING) & !type.equals(Consts.L_RATING)) {
            return null;
        }
        Statement stmt = c.createStatement();
        String sql = String.format("select distinct USER_ID, count(RATING_ID) as RATINGS from %s group by USER_ID",getTableFromType(type));

        ResultSet rs = stmt.executeQuery(sql);
        String user_id = null;
        int numRatings = 0;
        Map<String, Integer> map = new HashMap<>();
        while (rs.next()) {
            user_id = rs.getString("USER_ID");
            numRatings = rs.getInt("RATINGS");
            map.put(user_id, numRatings);
        }
        stmt.close();
        return map;
    }
    public  Map<List<String>,List<Integer>> getAllUsersMaxIterationPhotos(String type) throws SQLException {
        Map<String,Integer> map = getTotalRatingsAllUsers(type);
        Map<List<String>,List<Integer>> userMaxPhotosStatsMap = new HashMap<>();
        for(String userID : map.keySet()){
            String userGender = Users.getUserGender(userID, c);
            int userAllPhotos = getUnfilteredImagesToUser(userID, type).keySet().size();
            int userRatedPhotos = getRatedImagesWithMaxIterationByUser(userID, type).size();
            int currentIteration = getCurrentIterationForUser(userID, type);

            List<Integer> vlst = new ArrayList<>(4);
            vlst.add(currentIteration);
            vlst.add(userAllPhotos - userRatedPhotos);
            vlst.add(userRatedPhotos);
            vlst.add(userAllPhotos);
            List<String> klst = new ArrayList<>(2);
            klst.add(userID);
            klst.add(userGender);
            userMaxPhotosStatsMap.put(klst, vlst);
        }
        System.out.println("created successfully getAllUsersMaxIterationPhotos: " + type);
        return userMaxPhotosStatsMap;

    }



}
