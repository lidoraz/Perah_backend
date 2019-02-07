package com.hnn.dto;

public class User {


    private String user_id;
    private String fname;
    private String lname;
    private String dob;
    private String gender;
    private String sexual_oreintation;
    private String race;
    private String profession;

    public User(String user_id, String fname, String lname, String dob, String gender, String sexual_orientation, String race, String profession) {
        this.user_id = user_id;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.gender = gender;
        this.sexual_oreintation = sexual_orientation;
        this.race = race;
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "Users{" +
                "user_id='" + user_id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", sexual_orientation='" + sexual_oreintation + '\'' +
                ", proffesion='" + profession + '\'' +
                '}';
    }
}
