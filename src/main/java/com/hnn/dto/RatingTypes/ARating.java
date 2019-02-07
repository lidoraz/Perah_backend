package com.hnn.dto.RatingTypes;

import com.hnn.Consts;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class ARating extends Rating {
    public ARating() {
    }

    public ARating(String photoId, String userId, String ratingValue, String ratingUUID, String session, String iteration, String locationInSession, String timeTook, String timesUncertain, String phonePosition) {
        super(Consts.A_RATING, photoId, userId, ratingValue, ratingUUID, session, iteration, locationInSession, timeTook, timesUncertain, phonePosition);
    }

    @Override
    public String toString() {
        return "ARating{} " + super.toString();
    }
}
