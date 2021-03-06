package mz.project.cheaptrip.controllers;

import mz.project.cheaptrip.httpmodels.Location;
import mz.project.cheaptrip.services.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;

@RestController
public class LocationsController {

    @Autowired
    RoutesService routesService;

    private static final String LIMIT_DEFAULT = "10";
    private static final String TO = "to";
    private static final String FROM = "from";
    private static final String TYPE_MISMATCH = "Parameter 'type' must be 'from' or 'to'";

    @GetMapping("/locations")
    ResponseEntity getLocations(@RequestParam String type,
                                @RequestParam(name =  "search_name") String searchName,
                                @RequestParam(defaultValue = LIMIT_DEFAULT) int limit) {
        String typeStr = type.toLowerCase();
        if (!typeStr.equals(TO) && !typeStr.equals(FROM))
            return new ResponseEntity<>(TYPE_MISMATCH, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(routesService.getLocations(typeStr, searchName, limit), HttpStatus.OK);
    }

    @PostMapping("/locations")
    ResponseEntity<String> addLocations (@RequestBody Location location) {

        try {
            return new ResponseEntity<>(routesService.addLocations(location),
                    HttpStatus.CREATED);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>("Location with ID " + location.getId() + " already exist",
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

}
