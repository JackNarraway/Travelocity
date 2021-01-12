package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
@Path("category/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryController {
    @GET
    @Path("get/{CategoryID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetCategory(@PathParam("CategoryID") Integer CategoryID) {
        System.out.println("Invoked Category.GetCategory() with CategoryID " + CategoryID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT CategoryID, Title, Colour FROM Category WHERE CategoryID = ?");
            ps.setInt(1, CategoryID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()== true) {
                response.put("CategoryID", CategoryID);
                response.put("Title", results.getString(2));
                response.put("Colour", results.getInt(3));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    public String updateCategory(@FormDataParam("CategoryID") Integer CategoryID, @FormDataParam("Title") String Title, @FormDataParam("Colour") String Colour) {
        try {
            System.out.println("Invoked Users.UpdateUsers/update UserID=" + CategoryID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Category SET Title = ?, Colour = ? WHERE CategoryID = ?");
            ps.setString(1, Title);
            ps.setString(2, Colour);
            ps.setInt(3, CategoryID);
            ps.execute();
            return "{\"OK\": \"Category updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("add")
    public String CategoryAdd(@FormDataParam("Title") String Title, @FormDataParam("Colour") String Colour) {
        System.out.println("Invoked Users.UsersAdd()");
        LocalDate now = LocalDate.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Category (Title, Colour) VALUES (?, ?)");
            ps.setString(1, Title);
            ps.setString(2, Colour);
            ps.execute();
            return "{\"OK\": \"Added category.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/{CategoryID}")
    public String DeleteCategory(@PathParam("CategoryID") Integer CategoryID) throws Exception {
        System.out.println("Invoked Category.DeleteCategory()");
        if (CategoryID == null) {
            throw new Exception("CategoryID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Category WHERE CategoryID = ?");
            ps.setInt(1, CategoryID);
            ps.execute();
            return "{\"OK\": \"Category deleted\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

}
