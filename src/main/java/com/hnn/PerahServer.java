//package com.hnn;
//
//import com.hnn.dao.*;
//import com.hnn.dto.RatingTypes.ARating;
//import com.hnn.dto.RatingTypes.LRating;
//import com.hnn.dto.RatingTypes.Rating;
//import com.hnn.utils.Pair;
//
//
////import com.hnn.dao.Users;
//
//import java.io.IOException;
//import java.security.InvalidParameterException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.logging.Logger;
//
//public class PerahServer {
//    public static void main(String[] args) throws IOException, SQLException {
//
//        Logger logger = Logger.getLogger(PerahServer.class.getName());
//
//        Connection c = DatabaseOperations.initTables(Consts.TestDB);
//        DatabaseOperations.initPhotos(c, Consts.TestDB);
//
//        Users users = new Users(c);
//        Ratings ratings = new Ratings(c);
//        users.insertNewUser_debug("12","Male");
//
//        while (true) {
//
//            String currUser = "12";
//            if(!users.isRegistered(currUser)){
//                throw new InvalidParameterException();
//            }
//            // connect
//            //auth
//            // send images with the session id.
//            // finish images? after completing the task, button to again.
//            Pair ap = ratings.getImagesToUser(currUser, Consts.A_RATING);
//            String[] aImages = ap.getKey();
//            boolean aInceaseIt = ap.getValue();
//            Integer aSessionID = ratings.getCurrentSessionForUser(currUser,Consts.A_RATING) + 1;
//            Integer aIterationID = ratings.getCurrentIterationForUser(currUser,Consts.A_TABLE);
//            if(aInceaseIt){
//                aIterationID += 1;
//            }
//
//            Pair lp = ratings.getImagesToUser(currUser, Consts.L_RATING);
//            String[] lImages = lp.getKey();
//            boolean lInceaseIt = lp.getValue();
//            Integer lSessionID = ratings.getCurrentSessionForUser(currUser,Consts.L_RATING) + 1;
//            Integer lIterationID = ratings.getCurrentIterationForUser(currUser,Consts.L_TABLE);
//            if(lInceaseIt){
//                lIterationID += 1;
//            }
//            // rate !!!!
//
//            for(int i = 0 ; i < aImages.length ; i++){
//                String photo_id = aImages[0];
//                long start = System.currentTimeMillis();
//                // SHOW IMAGE HERE
//
//                //
//                String ratingV = Double.toString(Math.random()*5);
//                long end = System.currentTimeMillis();
//                String timeTook = Long.toString(end - start);
//                String uuid = Long.toString(System.currentTimeMillis());
//                Rating aRating = new ARating(photo_id,currUser,ratingV, uuid, Integer.toString(aSessionID),Integer.toString(aIterationID) , Integer.toString(i), timeTook, null, null);
//                ratings.insertRatingIntoDB(aRating);
//            }
//            for(int i = 0 ; i < lImages.length ; i++){
//                String photo_id = lImages[0];
//                long start = System.currentTimeMillis();
//                // SHOW IMAGE HERE
//
//                //
//                String ratingV = Double.toString(Math.random()*5);
//                long end = System.currentTimeMillis();
//                String timeTook = Long.toString(end - start);
//                String uuid = Long.toString(System.currentTimeMillis());
//                Rating lRating = new LRating(photo_id,currUser,ratingV, uuid, Integer.toString(lSessionID),Integer.toString(lIterationID) , Integer.toString(i), timeTook, null, null);
//                ratings.insertRatingIntoDB(lRating);
//            }
//
//
//
//
////            System.out.println("in TRUE");
////            Socket connectionSocket = welcomeSocket.accept();
////            System.out.println("accepted a client");
////            BufferedReader inFromClient =
////                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
////            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
////            clientSentence = inFromClient.readLine();
////            System.out.println("Received: " + clientSentence);
////            capitalizedSentence = clientSentence.toUpperCase() + '\n';
////            outToClient.writeBytes(capitalizedSentence);
//        }
//
//    }
//}
