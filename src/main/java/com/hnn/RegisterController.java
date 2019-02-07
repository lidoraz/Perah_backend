package com.hnn;

import java.sql.SQLException;

import com.hnn.dao.Users;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    private static boolean isUserFormOk(String user_id, String fname, String lname, String gender, String dob, String sexual_oreintation, String race, String profession) {
        if (user_id.length() == 0 ||
                fname.length() == 0 ||
                lname.length() == 0 ||
                gender.length() == 0 ||
                dob.length() == 0 ||
                sexual_oreintation.length() == 0 ||
                race.length() == 0 ||
                profession.length() == 0) {
            return false;
        }
        return true;
    }

    @CrossOrigin
    @RequestMapping("/reg")
    public int Register(
            @RequestParam(value = "user_id") String user_id,
            @RequestParam(value = "fname") String fname,
            @RequestParam(value = "lname") String lname,
            @RequestParam(value = "gender") String gender,
            @RequestParam(value = "dob") String dob,
            @RequestParam(value = "sexual_oreintation") String sexual_oreintation,
            @RequestParam(value = "race") String race,
            @RequestParam(value = "profession") String profession
    ) {

        try {

            // CHECK INPUT HERE

            Users users = Application.users;
            if (!isUserFormOk(user_id, fname, lname, gender, dob, sexual_oreintation, race, profession)) {
                return -2;// "One or more of the parameters are missing!"
            }
            if (users.isRegistered(user_id)) {
                return -1; // "User is already registered."
            }
            users.insertNewUser(user_id, fname, lname, dob, gender, sexual_oreintation, race, profession);
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -10;
    }
}