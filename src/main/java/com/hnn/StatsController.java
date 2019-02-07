package com.hnn;
//package hello;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.hnn.dao.Ratings;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {
    @CrossOrigin
    @RequestMapping("/stats")
    public String stats(@RequestParam(value = "user_id", defaultValue = "") String user_id) {
        if (user_id.length() == 0) {
            System.out.println("error in params in SessionController");
        }
        if (!user_id.equals("Shmulik")) {
            return "invalid username";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    "table, th, td {\n" +
                    "  border: 1px solid  black;\n" +
                    "}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>");
            sb.append(String.format("<h1>runID = %d </h1>", Application.runID));
            Ratings ratings = Application.ratings;
//            int totalImages =
            int ratingsA = ratings.getTotalRatings(Consts.A_RATING);
            int ratingsL = ratings.getTotalRatings(Consts.L_RATING);
            sb.append("TOTAL RATINGS: </br>");
            sb.append("#"+Consts.A_RATING+" ratings: ").append(ratingsA).append("</br>#"+Consts.L_RATING+" ratings: ").append(ratingsL);
            Map<String,Integer> AMap = ratings.getTotalRatingsAllUsers(Consts.A_RATING);
            Map<String,Integer> LMap = ratings.getTotalRatingsAllUsers(Consts.L_RATING);
            getTableInStringBuilder(sb, AMap, Consts.A_RATING);
            getTableInStringBuilder(sb, LMap, Consts.L_RATING);
            sb.append("</br> <h1>TOTAL RATING STATS PER USER</h1>");
            Map<List<String>, List<Integer>> progressAmap = ratings.getAllUsersMaxIterationPhotos(Consts.A_RATING);
            Map<List<String>, List<Integer>> progressLmap = ratings.getAllUsersMaxIterationPhotos(Consts.L_RATING);
            buildProgressMap(sb, progressAmap, Consts.A_RATING);
            buildProgressMap(sb, progressLmap, Consts.L_RATING);


            sb.append("</body>\n" +
                    "</html>\n");
            return sb.toString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void buildProgressMap(StringBuilder sb, Map<List<String>, List<Integer>> map, String title){
        sb.append("</br></br>").append(title).append(" Ratings:");
//        lst.add(currentIteration);
//        lst.add(userAllPhotos - userRatedPhotos);
//        lst.add(userRatedPhotos);
//        lst.add(userAllPhotos);
        sb.append("</br> <table><tr>" +
                "<th>user_id</th>" +
                "<th>gender</th>" +
                "<th>currentIteration</th>" +
                "<th>remainingPhotos</th>" +
                "<th>ratedPhotos</th>" +
                "<th>allPhotosForUser</th>" +
                "<th>progress %</th>" +
                "<th>hasFinishedOneIter?</th>" +
                "</tr>");
        for(Map.Entry<List<String>, List<Integer>> entry: map.entrySet()) {
            List<String> ks = entry.getKey();
            List<Integer> vals = entry.getValue();
            sb.append("<tr>");
            for(String k: ks){
                sb.append("<td>)");
                sb.append(k);
                sb.append("</td>");
            }
            for(Integer v: vals){
                sb.append("<td>)");
                sb.append(v);
                sb.append("</td>");
            }
            // Percentage
            double percent = vals.get(2)/(double)vals.get(3);
            percent = Math.round(percent * 100.0) / 100.0;
            sb.append("<td>").append(percent).append("%</td>");
            int currentIter = vals.get(0);
            int remainingPhotos = vals.get(1);
            sb.append("<td>").append((currentIter > 0 || remainingPhotos == 0)? "<b>YES</b>" : "NO").append("%</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");

    }
    public static void getTableInStringBuilder(StringBuilder sb, Map<String,Integer> map, String title){

        sb.append("</br></br>").append(title).append(" Ratings:");
        sb.append("</br> <table><tr><th>user_id</th><th>#Ratings</th></tr>");
        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            sb.append("<tr>");
            sb.append("<td>)");
            sb.append(entry.getKey());
            sb.append("</td>");
            sb.append("<td>)");
            sb.append(entry.getValue());
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
    }
}