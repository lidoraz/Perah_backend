package com.hnn.dao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnn.Consts;
import com.hnn.dto.RatingTypes.*;

import java.io.IOException;
import java.security.InvalidParameterException;

public class ResponseParser {
    static ObjectMapper mapper = new ObjectMapper();




    //get from client to server
    public static Rating gsonRatingToRating(String rating,String type) throws IOException {
        if(!type.equals(Consts.A_RATING) && (!type.equals(Consts.L_RATING))){
            throw new InvalidParameterException();
        }
        Rating rtg;
        if(type.equals(Consts.L_RATING)){
            rtg = mapper.readValue(rating, LRating.class);
        }
        else{
            rtg = mapper.readValue(rating, ARating.class);
        }
        System.out.println(rtg);
        return rtg;
    }

    //send json to client
    public static String ratingToJsonRating(Rating rating) throws IOException {
//        Gson gson = new Gson();
        String jsonInString = mapper.writeValueAsString(rating);
        return jsonInString;
    }
}
