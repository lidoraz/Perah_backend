package com.hnn.dto.RatingTypes;

import java.util.Objects;

public abstract class Rating {

    private String ratingType;

    private String photoId;
    private String userId;
    private String ratingValue;
    private String ratingUUID;
    private String session;
    private String iteration;
    private String locationInSession;
    private String timeTook;
    private String timesUncertain; // Did change his selection?
    private String phonePosition;

    public Rating() {
    }

    public Rating(String ratingType, String photoId, String userId, String ratingValue, String ratingUUID, String session, String iteration, String locationInSession, String timeTook, String timesUncertain, String phonePosition) {
        this.ratingType = ratingType;
        this.photoId = photoId;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.ratingUUID = ratingUUID;
        this.session = session;
        this.iteration = iteration;
        this.locationInSession = locationInSession;
        this.timeTook = timeTook;
        this.timesUncertain = timesUncertain;
        this.phonePosition = phonePosition;
    }


    @Override
    public String toString() {
        return "Rating{" +
                "photoId=" + photoId +
                ", userId=" + userId +
                ", ratingUUID=" + ratingUUID +
                ", session=" + session +
                ", iteration=" + iteration +
                ", locationInSession=" + locationInSession +
                ", timeTook=" + timeTook +
                ", timesUncertain=" + timesUncertain +
                ", phonePosition=" + phonePosition +
                ", ratingType='" + ratingType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;
        Rating rating = (Rating) o;
        return Objects.equals(ratingType, rating.ratingType) &&
                Objects.equals(photoId, rating.photoId) &&
                Objects.equals(userId, rating.userId) &&
                Objects.equals(ratingValue, rating.ratingValue) &&
                Objects.equals(ratingUUID, rating.ratingUUID) &&
                Objects.equals(session, rating.session) &&
                Objects.equals(iteration, rating.iteration) &&
                Objects.equals(locationInSession, rating.locationInSession) &&
                Objects.equals(timeTook, rating.timeTook) &&
                Objects.equals(timesUncertain, rating.timesUncertain) &&
                Objects.equals(phonePosition, rating.phonePosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratingType, photoId, userId, ratingValue, ratingUUID, session, iteration, locationInSession, timeTook, timesUncertain, phonePosition);
    }

    public String getRatingType() {
        return ratingType;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public String getRatingUUID() {
        return ratingUUID;
    }

    public String getSession() {
        return session;
    }

    public String getIteration() {
        return iteration;
    }

    public String getLocationInSession() {
        return locationInSession;
    }

    public String getTimeTook() {
        return timeTook;
    }

    public String getTimesUncertain() {
        return timesUncertain;
    }

    public String getPhonePosition() {
        return phonePosition;
    }
}