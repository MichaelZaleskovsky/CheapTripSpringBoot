package mz.project.cheaptrip.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import mz.project.cheaptrip.entities.CurrencyDb;
import mz.project.cheaptrip.entities.TransportDb;
import mz.project.cheaptrip.httpmodels.LineReq;
import mz.project.cheaptrip.httpmodels.Location;
import mz.project.cheaptrip.httpmodels.NewRoute;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbTransfer {

    @Autowired
    RoutesService service;

    private static final String sqlUrl = "jdbc:mysql://3.23.159.104:3306/cheap_trip?" +
            "user=root&password=12345";

    public long locations() {
        long result = 0;

        try (Connection conn = DriverManager.getConnection(sqlUrl)) {

// LOCATIONS
            String query = "SELECT * FROM locations";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.execute();
            ResultSet locationsResultSet = statement.getResultSet();
            List<Location> locs = new ArrayList<>();
            while (locationsResultSet.next()) {
                int id = locationsResultSet.getInt("id");
                String name = locationsResultSet.getString("name");
                System.out.println("Location read from DB: " + id);

                locs.add(new Location(id, name));
            }

            result = locs.stream()
                    .peek(loc -> {service.addLocations(loc);
                                  System.out.println("Sent to new DB: "+loc);})
                    .count();

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return result;
    }

    public long transports() {
        long result = 0;

        try (Connection conn = DriverManager.getConnection(sqlUrl)) {

// TRANSPORTATION_TYPE
            String query = "SELECT * FROM transportation_types";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.execute();
            ResultSet transportResultSet = statement.getResultSet();
            List<TransportDb> trans = new ArrayList<>();
            while (transportResultSet.next()) {
                int id = transportResultSet.getInt("id");
                String name = transportResultSet.getString("name");
                System.out.println("Location read from DB: " + id);

                trans.add(new TransportDb(id, name));
            }

            result = trans.stream()
                    .peek(tr -> {service.addTransport(tr);
                                  System.out.println("Sent to new DB: "+tr);})
                    .count();
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return result;
    }

    public long currencies() {
        long result = 0;

        try (Connection conn = DriverManager.getConnection(sqlUrl)) {

// CURRENCIES
            String query = "SELECT * FROM currencies";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.execute();
            ResultSet currenciesResultSet = statement.getResultSet();
            List<CurrencyDb> currs = new ArrayList<>();
            while (currenciesResultSet.next()) {
                int id = currenciesResultSet.getInt("id");
                String name = currenciesResultSet.getString("name");
                String code = currenciesResultSet.getString("code");
                String symbol = currenciesResultSet.getString("symbol");
                float oneEuroRate = currenciesResultSet.getFloat("one_euro_rate");
                String r2rSymbol = currenciesResultSet.getString("r2r_symbol");
                System.out.println("Location read from DB: " + id);

                currs.add(new CurrencyDb(id, name, code, symbol, r2rSymbol, new BigDecimal(oneEuroRate)));
            }

            result = currs.stream()
                    .peek(curr -> {service.addCurrency(curr);
                                  System.out.println("Sent to new DB: "+curr);})
                    .count();

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return result;
    }

    public long lines() {
        long result = 0;

// TRAVEL_DATA
        String query = "SELECT * FROM travel_data";

        try (Connection conn = DriverManager.getConnection(sqlUrl);
             PreparedStatement statement = conn.prepareStatement(query);) {

            statement.execute();
            ResultSet linesResultSet = statement.getResultSet();
            List<LineReq> lines = new ArrayList<>();
            while (linesResultSet.next()) {
                int id = linesResultSet.getInt("id");
                int transport = linesResultSet.getInt("transportation_type");
                int price = linesResultSet.getInt("price");
                int curr = linesResultSet.getInt("currency_id");
                String line = linesResultSet.getString("line");
                int time = linesResultSet.getInt("time_in_minutes");
                int from = linesResultSet.getInt("from");
                int to = linesResultSet.getInt("to");
                System.out.println("Location read from DB: " + id);

                lines.add(new LineReq(id, transport, new BigDecimal(price), curr, line, time, from, to));
            }

            result = lines.stream()
                    .peek(line -> {service.addLine(line);
                                  System.out.println("Sent to new DB: "+line);})
                    .count();

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return result;
    }

    public long routes() {
        long result = 0;

// fixed_routes flying_routes routes
        String queryGro = "SELECT * FROM fixed_routes";
        String queryFly = "SELECT * FROM flying_routes";
        String queryMix = "SELECT * FROM routes";

        try (Connection conn = DriverManager.getConnection(sqlUrl);
             PreparedStatement statementMix = conn.prepareStatement(queryMix);
             PreparedStatement statementFly = conn.prepareStatement(queryFly);
             PreparedStatement statementGro = conn.prepareStatement(queryGro);) {

            List<NewRoute> routes = new ArrayList<>();
            String routeType;

            statementMix.execute();
            ResultSet statementMixSet = statementMix.getResultSet();
            routeType = RoutesService.MIX_ROUTE;

            while (statementMixSet.next()) {
                String travelData = statementMixSet.getString("travel_data");
                int from = statementMixSet.getInt("from");
                int to = statementMixSet.getInt("to");
                System.out.println("Location read from DB: from " + from + " to " + to);

                routes.add(new NewRoute(from, to, routeType, travelData));
            }

            statementFly.execute();
            ResultSet statementFlySet = statementFly.getResultSet();
            routeType = RoutesService.FLY_ROUTE;

            while (statementFlySet.next()) {
                String travelData = statementFlySet.getString("travel_data");
                int from = statementFlySet.getInt("from");
                int to = statementFlySet.getInt("to");
                System.out.println("Location read from DB: from " + from + " to " + to);

                routes.add(new NewRoute(from, to, routeType, travelData));
            }

            statementGro.execute();
            ResultSet statementGroSet = statementGro.getResultSet();
            routeType = RoutesService.MIX_ROUTE;

            while (statementMixSet.next()) {
                String travelData = statementMixSet.getString("travel_data");
                int from = statementGroSet.getInt("from");
                int to = statementGroSet.getInt("to");
                System.out.println("Location read from DB: from " + from + " to " + to);

                routes.add(new NewRoute(from, to, routeType, travelData));
            }

            result = routes.stream()
                    .peek(route -> {
                        List<Long> list = Arrays.stream(route.getTravelData().split("."))
                                .map(Long::parseLong)
                                .collect(Collectors.toList());
                        service.addRoute(route, list);
                        System.out.println("Sent to new DB: "+route);})
                    .count();

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return result;
    }
}