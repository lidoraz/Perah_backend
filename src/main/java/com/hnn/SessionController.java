package com.hnn;
//package hello;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.atomic.AtomicLong;

import com.hnn.dao.Ratings;
import com.hnn.dao.Users;
import com.hnn.utils.Pair;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
    @CrossOrigin
    @RequestMapping("/sess")
    public Session session(@RequestParam(value="user_id", defaultValue="") String user_id, @RequestParam(value="type", defaultValue="") String type) {
        if(user_id.length() ==0 || type.length() == 0 ){
            System.out.println("error in params in SessionController");
        }
        try{
            Users users = Application.users;
            Ratings ratings = Application.ratings;

            if(!users.isRegistered(user_id)){
                System.out.println(String.format("Rejected  user: %s ",user_id));
                return new Session(user_id);
            }

            if(type.equals(Consts.A_RATING)){
                Pair ap = ratings.getImagesToUser(user_id, Consts.A_RATING);
                String[] aImages = ap.getKey();
                boolean aIncreaseIt = ap.getValue();
                int aSessionID = ratings.getCurrentSessionForUser(user_id,Consts.A_RATING) + 1;
                Integer aIterationID = ratings.getCurrentIterationForUser(user_id,Consts.A_RATING);
                if(aIncreaseIt){
                    aIterationID += 1;
                }
                System.out.println(String.format("Sent a new %s session for user: %s ", Consts.A_RATING,user_id));
                return new Session(user_id, type, aSessionID, aImages, aIterationID);
            }
            if(type.equals(Consts.L_RATING)){
                Pair lp = ratings.getImagesToUser(user_id, Consts.L_RATING);
                String[] lImages = lp.getKey();
                boolean lIncreaseIt = lp.getValue();
                int lSessionID = ratings.getCurrentSessionForUser(user_id,Consts.L_RATING) + 1;
                Integer lIterationID = ratings.getCurrentIterationForUser(user_id,Consts.A_RATING);
                if(lIncreaseIt){
                    lIterationID += 1;
                }
                System.out.println(String.format("Sent a new %s session for user: %s ", Consts.L_RATING,user_id));
                return new Session(user_id, type, lSessionID, lImages, lIterationID);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}