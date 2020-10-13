package dbinitializr;


import com.fasterxml.jackson.databind.ObjectMapper;
import mz.project.cheaptrip.httpmodels.LineReq;
import mz.project.cheaptrip.httpmodels.NewRoute;
import mz.project.cheaptrip.services.RoutesService;
import okhttp3.*;

import java.io.IOException;
//import static io.restassured.RestAssured.given;


public class DataLoader {

    private static final OkHttpClient httpClient = new OkHttpClient();
    static String domen ="http://localhost:8080";
    static ObjectMapper mapper = new ObjectMapper();


    public static void main(String[] args) throws IOException {
//  Add Location
        String location = "/locations";

        String[] jsons = {
                "{\"id\":100,\"name\":\"Antwerpen\"}",
                "{\"id\":101,\"name\":\"Annapurna\"}",
                "{\"id\":102,\"name\":\"Antananarivu\"}",
                "{\"id\":103,\"name\":\"Andijan\"}",
                "{\"id\":104,\"name\":\"Aberdin\"}",
                "{\"id\":105,\"name\":\"Athen\"}",
                "{\"id\":106,\"name\":\"Augsburg\"}",
                "{\"id\":107,\"name\":\"Antonopolis\"}",
                "{\"id\":108,\"name\":\"Alexandria\"}",
                "{\"id\":109,\"name\":\"Anadyr\"}",
                "{\"id\":110,\"name\":\"Acnorij\"}"
        };

        for (String json : jsons) {
            sendPost(json, location);
        }

        String getLocations = "/locations?type=from&search_name=a";
        String addLine = "/lines";
        LineReq[] lines = {
                new LineReq("Bus", 33.2, 123, 100, 105),
                new LineReq("Flight", 32.2, 113, 101,102),
                new LineReq("Car", 13.2, 23, 103,105),
                new LineReq("Bus", 53.5, 19, 104,110),
                new LineReq("Flight", 34.2, 90, 111,105),
                new LineReq("Flight", 133.2, 33, 105,110),
                new LineReq("Bus", 222.2, 112, 106,107)
        };

        for (LineReq line : lines) {
            sendPost(mapper.writeValueAsString(line), addLine);
        }

        String addRoute = "/routes";
        NewRoute[] routes = {
                new NewRoute(100, 102, RoutesService.MIX_ROUTE, "1, 2, 3"),
                new NewRoute(101, 103, RoutesService.MIX_ROUTE, "2, 3"),
                new NewRoute(102, 104, RoutesService.MIX_ROUTE, "7,4"),
                new NewRoute(103, 105, RoutesService.MIX_ROUTE, "5,6,1"),
                new NewRoute(104, 106, RoutesService.MIX_ROUTE, "1"),
                new NewRoute(105, 107, RoutesService.MIX_ROUTE, "5"),
                new NewRoute(106, 108, RoutesService.MIX_ROUTE, "2, 5, 7"),
                new NewRoute(107, 109, RoutesService.MIX_ROUTE, "9, 2, 3, 4"),
                new NewRoute(108, 110, RoutesService.MIX_ROUTE, "1, 2"),
                new NewRoute(109, 111, RoutesService.MIX_ROUTE, "2, 3"),
                new NewRoute(110, 101, RoutesService.MIX_ROUTE, "5, 3"),
                new NewRoute(111, 102, RoutesService.MIX_ROUTE, "1, 3"),
                new NewRoute(100, 102, RoutesService.FLY_ROUTE, "1, 2, 3"),
                new NewRoute(101, 103, RoutesService.FLY_ROUTE, "2, 3"),
                new NewRoute(102, 104, RoutesService.FLY_ROUTE, "7,4"),
                new NewRoute(103, 105, RoutesService.FLY_ROUTE, "5,6,1"),
                new NewRoute(104, 106, RoutesService.FLY_ROUTE, "1"),
                new NewRoute(105, 107, RoutesService.FLY_ROUTE, "5"),
                new NewRoute(106, 108, RoutesService.FLY_ROUTE, "2, 5, 7"),
                new NewRoute(107, 109, RoutesService.FLY_ROUTE, "9, 2, 3, 4"),
                new NewRoute(100, 102, RoutesService.GROUND_ROUTE, "1, 2, 3"),
                new NewRoute(101, 103, RoutesService.GROUND_ROUTE, "2, 3"),
                new NewRoute(102, 104, RoutesService.GROUND_ROUTE, "7,4"),
                new NewRoute(103, 105, RoutesService.GROUND_ROUTE, "5,6,1"),
                new NewRoute(104, 106, RoutesService.GROUND_ROUTE, "1"),
        };

        for (NewRoute route : routes) {
            sendPost(mapper.writeValueAsString(route), addRoute);
        }
    }

    private static void sendPost(String json, String endPoint) {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(domen + endPoint)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful())
                System.out.println("Unexpected code " + response);
            else
                // Get response body
                System.out.println("POST "+endPoint);
                System.out.println(response.body().string());
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendGet(String endPoint) {
        Request request = new Request.Builder()
                .url(domen + endPoint)
                .addHeader("Content-Type", "application/json")
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful())
                System.out.println("Unexpected code " + response);
            else
                // Get response body
                System.out.println("GET "+endPoint);
                System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
