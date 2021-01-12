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

    @GET
    @Path("get/{AchievementID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetUser(@PathParam("AchievementID") Integer AchievementID) {
        System.out.println("Invoked Users.GetUser() with UserID " + AchievementID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT  FROM Achievements WHERE AchievementID = ?");
            ps.setInt(1, AchievementID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()) {
                response.put("AchievementID", AchievementID);
                response.put("Title", results.getString(1));
                response.put("Description", results.getString(2));
                response.put("CategoryID", results.getInt(3));
                response.put("Requirements", results.getString(4));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    public String updateFood(@FormDataParam("AchievementID") Integer AchievementID, @FormDataParam("Title") String Title, @FormDataParam("Description") String Description, @FormDataParam("Requirements") String Requirements, @FormDataParam("CategoryID") Integer CategoryID) {
        try {
            System.out.println("Invoked Achievement.UpdateAchievement/update AchievementID=" + AchievementID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE List SET Title = ?, Description = ?, Requirements = ?, CategoryID = ? WHERE AchievementID = ?");
            ps.setString(1, Title);
            ps.setString(2, Description);
            ps.setString(3, Requirements);
            ps.setInt(4, CategoryID);
            ps.execute();
            return "{\"OK\": \"Achievements updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/{AchievementID}")
    public String DeleteUser(@PathParam("AchievementID") Integer AchievementID) throws Exception {
        System.out.println("Invoked Achievements.DeleteAchievement()");
        if (AchievementID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM List WHERE AchievementID = ?");
            ps.setInt(1, AchievementID);
            ps.execute();
            return "{\"OK\": \"Achievement deleted\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
}
