package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Path("userachievements/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class UserAchievementsController {
    @POST
    @Path("list/")
        public String UsersList(@FormDataParam("UserID") Integer UserID, @FormDataParam("AchievementID") Integer AchievementID) {
            System.out.println("Invoked UserAchievements.List()");
            JSONArray response = new JSONArray();
            try {
                PreparedStatement ps = Main.db.prepareStatement("SELECT Achieved FROM UserAchievements WHERE UserID = ? AND AchievementID = ?");
                ps.setInt(1, UserID);
                ps.setInt(2, AchievementID);
                ResultSet results = ps.executeQuery();
                JSONObject row = new JSONObject();
                if (results.next()) {
                    row.put("Achieved", "Achieved");
                    response.add(row);
                } else {
                    row.put("Achieved", "Not Achieved");
                    response.add(row);
                }
                return response.toString();
            } catch (Exception exception) {
                System.out.println("Database error: " + exception.getMessage());
                return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
            }
        }
    }

