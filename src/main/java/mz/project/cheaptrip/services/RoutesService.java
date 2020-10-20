package mz.project.cheaptrip.services;

import mz.project.cheaptrip.dao.DbAccess;
import mz.project.cheaptrip.entities.CurrencyDb;
import mz.project.cheaptrip.entities.LineDb;
import mz.project.cheaptrip.entities.RouteDb;
import mz.project.cheaptrip.entities.TransportDb;
import mz.project.cheaptrip.httpmodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoutesService {

    public static final String MIX_ROUTE = "Mixed route";
    public static final String FLY_ROUTE = "Flying route";
    public static final String GROUND_ROUTE = "Ground route";

    public static final String[] routeTypes = {MIX_ROUTE, FLY_ROUTE, GROUND_ROUTE};

    @Autowired
    DbAccess dbAccess;

    /*
    * Return List of Location, which contains searchName
    * */
    public List<Location> getLocations(String typeStr, String searchName, int limit) {
        List<Location> result = dbAccess
                .getLocations(typeStr, searchName, limit)
                .stream()
                .map(loc -> new Location(loc))
                .collect(Collectors.toList());

        return result;
    }

    /*
     * Add Location to Database, return string
     * */
    public String addLocations(Location location) {
        if(dbAccess.getLocation(location.getId()) != null) throw new EntityExistsException();
        return dbAccess.addLocation(location);
    }

    /*
     * Return List of Route with elements according roteTypes array
     * */
    public List<Route> getRoutes(int from, int to) {
        List<Route> result = Stream.of(routeTypes)
                .flatMap(routeType -> {
                    RouteDb r = dbAccess.getRouteDb(routeType, from, to);
                    return r == null ? Stream.empty() : Stream.of(r);
                })
                .map(rdb -> {
                    Route route = new Route(rdb);
                    for(LineRes l : route.getDirect_paths()){
                        l.setFrom(dbAccess.getLocation(l.getFrom_id()).getName());
                        l.setTo(dbAccess.getLocation(l.getTo_id()).getName());
                    };
                    return route;
                })
                .collect(Collectors.toList());

        return result;
    }

    /*
     * Add new Route to Database if it absent
     * */
    public void addRoute(NewRoute route, List<Long> linesId) {
        RouteDb routeDb = dbAccess.getRouteDb(route.getRouteType(), route.getFromId(), route.getToId());

        if(routeDb != null) throw new EntityExistsException();

        List<LineDb> lines = linesId.stream()
                .flatMap(id -> {
                    LineDb line = dbAccess.getLine(id);
                    return line == null ? Stream.empty() : Stream.of(line);
                })
                .collect(Collectors.toList());

        dbAccess.addRoute(route.getFromId(), route.getToId(), route.getRouteType(), lines);
    }

    public void addLine(LineReq line) {
        dbAccess.addLine(line);
    }

    public void UpdateRoute(NewRoute route, List<Long> linesId) {
        RouteDb routeDb = dbAccess.getRouteDb(route.getRouteType(), route.getFromId(), route.getToId());

        if(routeDb == null) throw new EntityNotFoundException();

        List<LineDb> lines = linesId.stream()
                .flatMap(id -> {
                    LineDb line = dbAccess.getLine(id);
                    return line == null ? Stream.empty() : Stream.of(line);
                })
                .collect(Collectors.toList());

        dbAccess.UpdateRoute(routeDb, lines);
    }

    public void deleteRoute(NewRoute route) {
        RouteDb routeDb = dbAccess.getRouteDb(route.getRouteType(), route.getFromId(), route.getToId());

        if(routeDb == null) throw new EntityNotFoundException();

        dbAccess.deleteRoute(routeDb);
    }

    public List<LineRes> getAllLinesFrom(long locationId) {
        LineDb line = dbAccess.getLine(locationId);
        if(line == null) throw new EntityNotFoundException();

        List<LineRes> result = dbAccess.getAllLinesFrom(locationId)
                .stream()
                .map(LineRes::new)
                .collect(Collectors.toList());
        return result;
    }

    public void addTransport(TransportDb transport) {
        dbAccess.addTransport(transport);
    }

    public List<TransportDb> getTransports() {
        return dbAccess.getTransports();
    }

    public void addCurrency(CurrencyDb curr) {
        dbAccess.addCurrency(curr);
    }

    public List<CurrencyDb> getCurrencies() {
        return dbAccess.getCurrencies();
    }
}
