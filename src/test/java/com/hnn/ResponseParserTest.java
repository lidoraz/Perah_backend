package com.hnn;

import com.hnn.dao.ResponseParser;
import com.hnn.dto.RatingTypes.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ResponseParserTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void gsonRatingToRating() throws IOException {
        String str = "{\"ratingType\":\"ATTRACTIVENESS\",\"photoId\":\"43\",\"userId\":\"12\",\"ratingValue\":\"5\",\"ratingUUID\":\"2352135\",\"session\":\"3\",\"iteration\":\"1\",\"locationInSession\":\"4\",\"timeTook\":\"10\",\"timesUncertain\":\"10\",\"phonePosition\":null}";
        Rating rtg = ResponseParser.gsonRatingToRating(str, Consts.A_RATING);
        Rating rtgOrigin = new ARating("43",  "12",  "5",  "2352135",  "3",  "1",  "4",  "10",  "10", null);
        assertEquals(rtg,rtgOrigin);
    }

    @Test
    void ratingToJsonRating() throws IOException {
        Rating rtg = new ARating("43",  "12",  "5",  "2352135",  "3",  "1",  "4",  "10",  "10", null);
        String json = ResponseParser.ratingToJsonRating(rtg);
        System.out.println(json);
        String str = "{\"ratingType\":\"ATTRACTIVENESS\",\"photoId\":\"43\",\"userId\":\"12\",\"ratingValue\":\"5\",\"ratingUUID\":\"2352135\",\"session\":\"3\",\"iteration\":\"1\",\"locationInSession\":\"4\",\"timeTook\":\"10\",\"timesUncertain\":\"10\",\"phonePosition\":null}";

        assertEquals(json, str);
//        json.equals()
    }
}