package controllers;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Path("list/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class ListController {
    @GET
    @Path("list/{ListID}")
    public String ShowList(@PathParam("ListID") int ListID) {
        System.out.println("Invoked List.List()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT ListID, CategoryID, Title, Details, DateTime, UserID FROM List WHERE ListID = ?");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject row = new JSONObject();
                row.put("ListID", results.getInt(1));
                row.put("CategoryID", results.getInt(2));
                row.put("Title", results.getString(3));
                row.put("Details", results.getString(4));
                row.put("DateTime", results.getString(5));
                row.put("UserID", results.getInt(6));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @POST
    @Path("add")
    public String UsersAdd(@CookieParam("UserID") int UserID, @FormDataParam("CategoryID") Integer CategoryID, @FormDataParam("Title") String Title, @FormDataParam("Details") String Details, @FormDataParam("DateTime") String DateTime) {
        System.out.println("Invoked List.Add()");
        LocalDate now = LocalDate.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO List (UserID, CategoryID, Title, Details, DateTime) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, UserID);
            ps.setInt(2, CategoryID);
            ps.setString(3, Title);
            ps.setString(4, Details);
            ps.setString(5, DateTime);
            ps.execute();
            return "{\"OK\": \"Added list.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/{ListID}")
    public String DeleteUser(@PathParam("ListID") Integer ListID) throws Exception {
        System.out.println("Invoked Users.DeleteList()");
        if (ListID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM List WHERE ListID = ?");
            ps.setInt(1, ListID);
            ps.execute();
            return "{\"OK\": \"List deleted\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    public String updateFood(@CookieParam("UserID") Integer UserID, @FormDataParam("ListID") Integer ListID, @FormDataParam("CategoryID") Integer CategoryID, @FormDataParam("Title") String Title, @FormDataParam("Details") String Details) {
        try {
            System.out.println("Invoked Users.UpdateUsers/update UserID=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE List SET Title = ?, Details = ?, CategoryID = ? WHERE UserID = ? AND ListID = ?");
            ps.setString(1, Title);
            ps.setString(2, Details);
            ps.setInt(3, CategoryID);
            ps.execute();
            return "{\"OK\": \"List updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
}
