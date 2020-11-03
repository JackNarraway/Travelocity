package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("users/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class UsersController{
    @GET
    @Path("list")
    public String UsersList() {
        System.out.println("Invoked Users.UsersList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, EmailAddress, FirstName, LastName, ValidatedDate, Admin, Password, SessionToken FROM Users");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject row = new JSONObject();
                row.put("UserID", results.getInt(1));
                row.put("EmailAddress", results.getString(2));
                row.put("FirstName", results.getString(3));
                row.put("LastName", results.getString(4));
                row.put("ValidatedDate", results.getString(5));
                row.put("Admin", results.getBoolean(6));
                row.put("Password", results.getString(7));
                row.put("SessionToken", results.getString(8));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }
    @GET
    @Path("get/{UserID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetUser(@PathParam("UserID") Integer UserID) {
        System.out.println("Invoked Users.GetUser() with UserID " + UserID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT FirstName, LastName FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()) {
                response.put("UserID", UserID);
                response.put("FirstName", results.getString(1));
                response.put("LastName", results.getString(2));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("add")
    public String UsersAdd(@FormDataParam("EmailAddress") String EmailAddress, @FormDataParam("FirstName") String FirstName, @FormDataParam("LastName") String LastName, @FormDataParam("ValidatedDate") String ValidatedDate, @FormDataParam("Admin") Boolean Admin, @FormDataParam("Password") String Password) {
        System.out.println("Invoked Users.UsersAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (EmailAddress, FirstName, LastName, ValidatedDate, Admin, Password) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, EmailAddress);
            ps.setString(2, FirstName);
            ps.setString(3, LastName);
            ps.setString(4, ValidatedDate);
            ps.setBoolean(5, Admin);
            ps.setString(6, Password);
            ps.execute();
            return "{\"OK\": \"Added user.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("update")
    public String updateFood(@FormDataParam("UserID") Integer UserID, @FormDataParam("EmailAddress") String EmailAddress, @FormDataParam("Password") String Password) {
        try {
            System.out.println("Invoked Users.UpdateUsers/update UserID=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET EmailAddress = ?, Password = ? WHERE UserID = ?");
            ps.setString(1, EmailAddress);
            ps.setString(2, Password);
            ps.setInt(3, UserID);
            ps.execute();
            return "{\"OK\": \"Users updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("delete/{UserID}")
    public String DeleteUser(@PathParam("UserID") Integer UserID) throws Exception {
        System.out.println("Invoked Users.DeleteUser()");
        if (UserID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.execute();
            return "{\"OK\": \"User deleted\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

}


