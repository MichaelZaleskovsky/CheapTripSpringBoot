package mz.project.cheaptrip.controllers;

import mz.project.cheaptrip.httpmodels.LineReq;
import mz.project.cheaptrip.services.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/lines")
public class LinesController {

    @Autowired
    RoutesService routesService;

    @PostMapping
    String addLine(@RequestBody LineReq line){
        routesService.addLine(line);
        return "Line added successful!";
    }

    @GetMapping("/all/{locationId}")
    ResponseEntity getAllLinesFrom(@PathVariable long locationId){
        ResponseEntity result;
        try {
            result = new ResponseEntity(routesService.getAllLinesFrom(locationId), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            result = new ResponseEntity("Location with ID "+locationId+" not found", HttpStatus.NOT_FOUND);
        }

        return result;
    }
}
