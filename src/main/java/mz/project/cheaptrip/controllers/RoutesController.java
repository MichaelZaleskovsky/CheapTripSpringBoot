package mz.project.cheaptrip.controllers;

import mz.project.cheaptrip.httpmodels.NewRoute;
import mz.project.cheaptrip.httpmodels.Route;
import mz.project.cheaptrip.services.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/routes")
public class RoutesController {

    @Autowired
    RoutesService routesService;

    /*
     * Return routes between 2 locations
     * GET host/routes?from=101&to=102
     * RETURN [Route1, Route2, Route3]
     **/
    @SuppressWarnings("rawtypes")
    @GetMapping
    ResponseEntity<List<Route>> getRoutes(@RequestParam(defaultValue = "json") String format,
                             @RequestParam int from,
                             @RequestParam int to){

        ResponseEntity<List<Route>> result = new ResponseEntity<>(routesService.getRoutes(from, to), HttpStatus.OK);

        return result;
    }

    /*
     * Add new route between 2 locations. Successful only if no such route in database
     * POST host/routes
     * BODY NewRoute
     * RETURN message
     * SUCCESS Status 200
     * ERROR Status 406
     **/
    @PostMapping
    ResponseEntity<String> addRoute(@RequestBody NewRoute route){
        String resStr = "Route added successfully!";

        try {
            List<Long> linesId = Arrays.stream(route.getTravelData().split(","))
                    .map(str -> Long.parseLong(str.trim()))
                    .collect(Collectors.toList());

            if(!Arrays.asList(RoutesService.routeTypes).contains(route.getRouteType())){
                throw new Exception("Available values for routeType is "
                        + String.join(", ", RoutesService.routeTypes));
            } else {
                routesService.addRoute(route, linesId);
            }

            return new ResponseEntity<>(resStr, HttpStatus.OK);

        } catch (EntityExistsException e) {
            resStr = "Route from "+route.getFromId()+" to "
                    +route.getToId()+" with type "
                    +route.getRouteType()+" already present in data base";
        } catch (Exception e) {
            resStr = "Available values for routeType is "
                    + String.join(", ", RoutesService.routeTypes)
                    + " and TavelData must content only numbers with delimiter `,` ";
        }

        return new ResponseEntity<>("resStr", HttpStatus.NOT_ACCEPTABLE);
    }

    /*
     * Update lines in distinct route between 2 locations. Successful only if such route presented in database
     * PUT host/routes
     * BODY NewRoute
     * RETURN message
     * SUCCESS Status 200
     * ERROR Status 404
     **/
    @PutMapping
    ResponseEntity<String> updateRoute(@RequestBody NewRoute route){
        String resStr = "Route updated successfully!";

        try {
            List<Long> linesId = Arrays.stream(route.getTravelData().split(","))
                    .map(str -> Long.parseLong(str.trim()))
                    .collect(Collectors.toList());

            if(!Arrays.asList(RoutesService.routeTypes).contains(route.getRouteType())){
                throw new Exception();
            } else {
                routesService.UpdateRoute(route, linesId);
            }

            return new ResponseEntity<>(resStr, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            resStr = "Route from "+route.getFromId()+" to "
                    +route.getToId()+" with type "
                    +route.getRouteType()+" not found in data base";
        } catch (Exception e) {
            resStr = "Available values for routeType is "
                    + String.join(", ", RoutesService.routeTypes)
                    + " and TavelData must content only numbers with delimiter `,` ";
        }

        return new ResponseEntity<>("resStr", HttpStatus.NOT_FOUND);
    }

    /*
     * Delete route between 2 locations. Successful only if such route presented in database
     * DELETE host/routes
     * BODY NewRoute
     * RETURN message
     * SUCCESS Status 200
     * ERROR Status 404
     **/
    @DeleteMapping
    ResponseEntity<String> deleteRoute(@RequestBody NewRoute route) {
        String resStr = "Route deleted successfully!";

        try {
            routesService.deleteRoute(route);
            return new ResponseEntity<>("Route deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Route from "+route.getFromId()+" to "
                    +route.getToId()+" with type "
                    +route.getRouteType()+" not found in data base", HttpStatus.NOT_FOUND);
        }
    }
}
