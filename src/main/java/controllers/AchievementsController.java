package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Path("achievements/")
public class AchievementsController {
    @POST
    @Path("add")
    public String UsersAdd(@FormDataParam("AchievementID") Integer AchievementID, @FormDataParam("Title") String Title, @FormDataParam("Description") String Description, @FormDataParam("CategoryID") Integer CategoryID, @FormDataParam("Requirements") Integer Requirements) {
        System.out.println("Invoked Users.AchievementAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Achievements (AchievementID, Title, Description, CategoryID, Requirements) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, AchievementID);
            ps.setString(2, Title);
            ps.setString(3, Description);
            ps.setInt(4, CategoryID);
            ps.setInt(5, Requirements);
            ps.execute();
            return "{\"OK\": \"Added achievement.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }
}
