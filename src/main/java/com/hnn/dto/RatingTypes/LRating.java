package com.hnn.dto.RatingTypes;

import com.hnn.Consts;

public class LRating extends Rating {

    public LRating() {
    }

    public LRating(String photoId, String userId, String ratingValue, String ratingUUID, String session, String iteration, String locationInSession, String timeTook, String timesUncertain, String phonePosition) {
        super(Consts.L_RATING, photoId, userId, ratingValue, ratingUUID, session, iteration, locationInSession, timeTook, timesUncertain, phonePosition);
    }

    @Override
    public String toString() {
        return "LRating{} " + super.toString();
    }
}
