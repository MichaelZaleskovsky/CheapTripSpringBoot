package mz.project.cheaptrip.controllers;

import mz.project.cheaptrip.httpmodels.LineReq;
import mz.project.cheaptrip.httpmodels.LineRes;
import mz.project.cheaptrip.services.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.swing.plaf.BorderUIResource;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LinesController {

    @Autowired
    RoutesService routesService;

    /*
    * Add new line into Database
    * POST host/lines
    * BODY LineReq
    * RESPONSE String message
    **/
    @PostMapping
    String addLine(@RequestBody LineReq line){
        routesService.addLine(line);
        return "Line added successful!";
    }

    /*
     * Get all lines from Locatioi by it's ID
     * POST host/lines/all/{locationId}
     * PARAMETER locationId - number
     * RESPONSE:
     *  STATUS 200 - List<LineRes>
     *  STATUS 404 - String message
     **/
    @GetMapping("/all/{locationId}")
    ResponseEntity getAllLinesFrom(@PathVariable long locationId){
        ResponseEntity result;
        try {
            List<LineRes> list = routesService.getAllLinesFrom(locationId);
            result = new ResponseEntity(list, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            result = new ResponseEntity("Location with ID "+locationId+" not found", HttpStatus.NOT_FOUND);
        }

        return result;
    }
}
