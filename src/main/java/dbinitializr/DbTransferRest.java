package dbinitializr;

import com.fasterxml.jackson.databind.ObjectMapper;
import mz.project.cheaptrip.services.RoutesService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbTransferRest {

    private static final OkHttpClient httpClient = new OkHttpClient();
    //        static String domen ="http://localhost:8080";
    static String domen ="http://52.14.161.122:8080";
    static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }

        try (Connection conn =
                     DriverManager.getConnection("jdbc:mysql://3.23.159.104:3306/cheap_trip?" +
                        "user=root&password=12345")) {

// LOCATIONS
            List<String> locations = getLocations(conn);
            locations.forEach(json -> {sendPost(json, "/locations");
                System.out.println("Sent to new DB: "+json);});

// TRANSPORTATION_TYPE
            List<String> transports = getTransports(conn);
            transports.forEach(json -> {sendPost(json, "/transports");
                System.out.println("Sent to new DB: "+json);});

// CURRENCIES
            List<String> currs = getCurrencies(conn);
            currs.forEach(json -> {sendPost(json, "/currencies");
                System.out.println("Sent to new DB: "+json);});

// TRAVEL_DATA
            List<String> lines = getLines(conn);
            lines.forEach(json -> {sendPost(json, "/lines");
                System.out.println("Sent to new DB: "+json);});

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    private static List<String> getLocations(Connection conn) throws SQLException {
        String query = "SELECT * FROM locations";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.execute();
        ResultSet locationsResultSet = statement.getResultSet();
        List<String> jsons = new ArrayList<>();
        String json;
        while (locationsResultSet.next()) {
            int id = locationsResultSet.getInt("id");
            String name = locationsResultSet.getString("name");
            json = "{\"id\":" + id + ",\"name\":\"" + name + "\"}";
            System.out.println("Location read from DB: " + json);
            jsons.add(json);
        }
        return jsons;
    }

    private static List<String> getTransports(Connection conn) throws SQLException {
        String query = "SELECT * FROM transportation_types";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.execute();
        ResultSet locationsResultSet = statement.getResultSet();
        List<String> jsons = new ArrayList<>();
        String json;
        while (locationsResultSet.next()) {
            int id = locationsResultSet.getInt("id");
            String name = locationsResultSet.getString("name");
            json = "{\"id\":" + id + ",\"name\":\"" + name + "\"}";
            System.out.println("Location read from DB: " + json);
            jsons.add(json);
        }
        return jsons;
    }

    private static List<String> getCurrencies(Connection conn) throws SQLException {
        String query = "SELECT * FROM currencies";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.execute();
        ResultSet locationsResultSet = statement.getResultSet();
        List<String> jsons = new ArrayList<>();
        String json;
        while (locationsResultSet.next()) {
            int id = locationsResultSet.getInt("id");
            String name = locationsResultSet.getString("name");
            String code = locationsResultSet.getString("code");
            String symbol = locationsResultSet.getString("symbol");
            float oneEuroRate = locationsResultSet.getFloat("one_euro_rate");
            String r2rSymbol = locationsResultSet.getString("r2r_symbol");
            json = "{\"id\":" + id +
                    ",\"name\":\"" + name + "\"" +
                    ",\"code\":\"" + code + "\"" +
                    ",\"symbol\":\"" + symbol + "\"" +
                    ",\"oneEuroRate\":" + oneEuroRate +
                    ",\"r2rSymbol\":\"" + r2rSymbol + "\"}";
            System.out.println("Location read from DB: " + json);
            jsons.add(json);
        }
        return jsons;
    }

    private static List<String> getLines(Connection conn) throws SQLException {
        String query = "SELECT * FROM travel_data";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.execute();
        ResultSet locationsResultSet = statement.getResultSet();
        List<String> jsons = new ArrayList<>();
        String json;
        while (locationsResultSet.next()) {
            int id = locationsResultSet.getInt("id");
            int from = locationsResultSet.getInt("from");
            int to = locationsResultSet.getInt("to");
            int transport = locationsResultSet.getInt("transportation_type");
            int time = locationsResultSet.getInt("time_in_minutes");
            int price = locationsResultSet.getInt("price");
            int curr = locationsResultSet.getInt("currency_id");
            String line = locationsResultSet.getString("line");

/*
            private long id;
            private long transportType;
            private BigDecimal price;
            private long currencyId;
            private String line;
            private int duration_minutes;
            private long from_id;
            private long to_id;
*/


            json = "{\"id\":" + id +
                    ",\"transportType\":" + transport +
                    ",\"price\":" + price +
                    ",\"from_id\":" + from +
                    ",\"to_id\":" + to +
                    ",\"currencyId\":" + curr +
                    ",\"duration_minutes\":" + time +
                    ",\"line\":\"" + line + "\"}";
//            System.out.println("Travel_data read from DB: " + json);
            jsons.add(json);
        }
        return jsons;
    }

    private static void sendPost(String json, String endPoint) {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .url(domen + endPoint)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful())
                System.out.println(endPoint + " UNSUCCESSFUL sending " + json);
            else
                // Get response body
                System.out.println(endPoint + " successful POST sending " + json);
            System.out.println("_________________________________________________________________________");
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

}