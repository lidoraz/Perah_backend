package com.hnn.dto;

public class Photo {

    private Integer imageId;
    private String path;
    private String gender;
    private Integer dob;
    private String origin;
    private Integer glasses;
    //TODO: add more features

    public Photo(Integer imageId, String path, String gender, Integer dob, String origin, Integer glasses) {
        this.imageId = imageId;
        this.path = path;
        this.gender = gender;
        this.dob = dob;
        this.origin = origin;
        this.glasses = glasses;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "imageId=" + imageId +
                ", path='" + path + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", origin='" + origin + '\'' +
                ", glasses=" + glasses +
                '}';
    }
}