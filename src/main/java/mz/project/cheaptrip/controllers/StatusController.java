package mz.project.cheaptrip.controllers;

import mz.project.cheaptrip.dao.DbAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusController {

    @Autowired
    DbAccess dbAccess;

    @Value("${active.profile}")
    private String profile;

    @GetMapping("/status")
    public String getStatus(){
        String res = "Server is active \n";
        try {
            List<String> r = dbAccess.checkStatus();
            res += "Database is active \nTables: " + r.stream().reduce((a,b) -> a + ", " + b);
        } catch (Exception e) {
            res += "Database is failured";
        }
        return res + "\nProfile: " + profile;
    }
}
