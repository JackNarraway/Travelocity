package controllers;

import oracle.jrockit.jfr.FlightRecorder;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import server.Main;

import javax.print.attribute.standard.Destination;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Path("flights/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class FlightsController {
    @GET
    @Path("get/{FlightID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetFlight(@PathParam("FlightID") Integer FlightID ) {
        System.out.println("Invoked Flights.GetFlight() with FlightID " + FlightID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT FlightDate, Destination, Departure, UserID FROM Flights WHERE FlightID = ?");
            ps.setInt(1, FlightID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()) {
                response.put("FlightID", FlightID);
                response.put("FlightDate", results.getString(2));
                response.put("Destination", results.getString(3));
                response.put("Departure", results.getString(4));
                response.put("UserID", results.getInt(5));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("add")
    public String FlightsAdd(@FormDataParam("EmailAddress") String FlightDate, @FormDataParam("FirstName") String Departures, @FormDataParam("LastName") String Destination, @FormDataParam("UserID") Integer UserID) {
        System.out.println("Invoked Users.UsersAdd()");
        LocalDate now = LocalDate.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Flights (FlightDate, Departures, Destination, UserID) VALUES (?, ?, ?, ?)");
            ps.setString(1, FlightDate);
            ps.setString(2, Departures);
            ps.setString(3, Destination);
            ps.setInt(4, UserID);
            ps.execute();
            return "{\"OK\": \"Added flight.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    public String UpdateFlight(@FormDataParam("FlightID") Integer FlightID, @FormDataParam("FlightDate") String FlightDate, @FormDataParam("Departures") String Departures, @FormDataParam("Destination") String Destination, @FormDataParam("UserID") Integer UserID) {
        try {
            System.out.println("Invoked Users.UpdateFlights/update FlightID=" + FlightID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Flights SET FlightDate = ?, Departures = ?, Destination = ? WHERE FlightID = ? AND UserID = ?");
            ps.setString(1, FlightDate);
            ps.setString(2, Departures);
            ps.setString(3, Destination);
            ps.setInt(4, FlightID);
            ps.setInt(5, UserID);
            ps.execute();
            return "{\"OK\": \"Flights updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/{FlightID}")
    public String DeleteFlight(@PathParam("FlightID") Integer FlightID) throws Exception {
        System.out.println("Invoked Users.DeleteFlight()");
        if (FlightID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Flights WHERE FlightID = ?");
            ps.setInt(1, FlightID);
            ps.execute();
            return "{\"OK\": \"User deleted\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }


}
