package com.hnn;

public class Session {

    private final long sessionId ;
    private final String[] imagesPath;
    private int iterationId ;
    private String sessionType;
    private String userId;

    public Session(String userId, String sessionType, long sessionId, String[] imagesPath, int iterationId) {
        this.userId = userId;
        this.sessionType = sessionType;
        this.sessionId = sessionId;
        this.iterationId = iterationId;
        this.imagesPath = imagesPath;
    }
    public Session(String userId){
        this.sessionType = "not registered";
        this.sessionId = -1;
        this.iterationId = -1;
        this.imagesPath = null;
        this.userId = userId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public String[] getImagesPath() {
        return imagesPath;
    }

    public int getIterationId() {
        return iterationId;
    }

    public String getSessionType() {
        return sessionType;
    }

    public String getUserId() {
        return userId;
    }
}