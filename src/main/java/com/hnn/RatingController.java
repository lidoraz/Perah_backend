package com.hnn;
//package hello;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.atomic.AtomicLong;

import com.hnn.dao.Ratings;
import com.hnn.dao.Users;
import com.hnn.dto.RatingTypes.ARating;
import com.hnn.dto.RatingTypes.LRating;
import com.hnn.dto.RatingTypes.Rating;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {
    @CrossOrigin
//    @RequestMapping(value = "/rate", method = RequestMethod.GET)
    @RequestMapping("/rate")
    public String rating(
            @RequestParam(value = "s") String ratingType,
                         @RequestParam(value = "photoId" ) String photoId,
                         @RequestParam(value = "user_id") String user_id,
                         @RequestParam(value = "ratingValue") String ratingValue,
                         @RequestParam(value = "ratingUUID") String ratingUUID,
                         @RequestParam(value = "session") String session,
                         @RequestParam(value = "iteration") String iteration,
                         @RequestParam(value = "locationInSession") String locationInSession,
                         @RequestParam(value = "timeTook") String timeTook,
                         @RequestParam(value = "timesUncertain") String timesUncertain,
                         @RequestParam(value = "phonePosition") String phonePosition
    ) {

        try {
            Users users = Application.users;
            Ratings ratings = Application.ratings;
            if (!users.isRegistered(user_id)) {
                return "not registered";
            }
            System.out.println(String.format("got a rating - form user= %s type= %s ",user_id,ratingType));
            Rating rtg = null;
            if (ratingType.equals(Consts.A_RATING)) {
                rtg = new ARating(photoId, user_id, ratingValue, ratingUUID, session, iteration, locationInSession, timeTook, timesUncertain, phonePosition);
            }
            if (ratingType.equals(Consts.L_RATING)) {
                rtg = new LRating(photoId, user_id, ratingValue, ratingUUID, session, iteration, locationInSession, timeTook, timesUncertain, phonePosition);
            }
            System.out.println(rtg.toString());
            ratings.insertRatingIntoDB(rtg);
            return "success for rating:" + photoId + "_"+ user_id;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}