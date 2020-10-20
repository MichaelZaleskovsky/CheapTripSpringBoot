package mz.project.cheaptrip.controllers;

import mz.project.cheaptrip.entities.CurrencyDb;
import mz.project.cheaptrip.entities.TransportDb;
import mz.project.cheaptrip.services.DbTransfer;
import mz.project.cheaptrip.services.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityExistsException;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    RoutesService service;

    @Autowired
    DbTransfer transfer;

    @PostMapping("/transports")
    private ResponseEntity<String> addTransport(@RequestBody TransportDb transport) {
        ResponseEntity<String> result;
        try {
            service.addTransport(transport);
            result = new ResponseEntity<>("Transport type " + transport.toString() + " added successfully", HttpStatus.OK);
        } catch (EntityExistsException e) {
            result = new ResponseEntity<>("Transport type " + transport.toString() + " already exist", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            result = new ResponseEntity<>("Internal mistake", HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    @GetMapping("/transports")
    private List<TransportDb> getTransports() {
        return service.getTransports();
    }

    @PostMapping("/currencies")
    private ResponseEntity<String> addCurrency(@RequestBody CurrencyDb curr) {
        ResponseEntity<String> result;
        try {
            service.addCurrency(curr);
            result = new ResponseEntity<>("Currency type " + curr.toString() + " added successfully", HttpStatus.OK);
        } catch (EntityExistsException e) {
            result = new ResponseEntity<>("Currency type " + curr.toString() + " already exist", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            result = new ResponseEntity<>("Internal mistake", HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    @GetMapping("/currencies")
    private List<CurrencyDb> getCurrenciess() {
        return service.getCurrencies();
    }

    @GetMapping("/update/locations")
    private String updateLocations(){
        return "Updated "+transfer.locations()+" locations";
    }

    @GetMapping("/update/transports")
    private String updateTransports(){
        return "Updated "+transfer.transports()+" transports type";
    }

    @GetMapping("/update/currencies")
    private String updateCurrencies(){
        return "Updated "+transfer.currencies()+" currencies type";
    }

    @GetMapping("/update/lines")
    private String updateLines(){
        return "Updated "+transfer.lines()+" lines";
    }

}