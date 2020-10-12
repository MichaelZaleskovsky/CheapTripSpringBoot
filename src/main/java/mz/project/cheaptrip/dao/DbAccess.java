package mz.project.cheaptrip.dao;

import mz.project.cheaptrip.entities.LineDb;
import mz.project.cheaptrip.entities.LocationDb;
import mz.project.cheaptrip.entities.RouteDb;
import mz.project.cheaptrip.entities.RouteKey;
import mz.project.cheaptrip.httpmodels.LineReq;
import mz.project.cheaptrip.httpmodels.Location;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DbAccess {

    @PersistenceContext
    EntityManager em;

    public List<LocationDb> getLocations(String typeStr, String searchName, int limit) {

        Location[] locs = {
                new Location(100, "Moscow"),
                new Location(101, "Tel-Aviv"),
                new Location(102, "London"),
                new Location(103, "Beijing"),
                new Location(104, "Paris"),
                new Location(105, "Berlin"),
                new Location(106, "Krakow")
        };

        String locSql = "SELECT * FROM location_db WHERE id IN (SELECT `" + typeStr + "` FROM route_db) " +
                "AND `name` LIKE '%" + searchName + "%' " +
                "ORDER BY CASE WHEN name LIKE '" + searchName + "%' THEN 0 ELSE 1 END LIMIT " + limit;

        List<LocationDb> locations = em.createNativeQuery(locSql, LocationDb.class)
                .getResultList();

        String locJpql = "SELECT l FROM LocationDb l ";
        return locations;
    }

/*
    public Route getRoute(String routeType, int from, int to) {
        String routesHpql = "SELECT routesDb FROM RoutesDb routesDb WHERE " +
                "routesDb.from = :from AND " +
                "routesDb.to = :to AND " +
                "routesDb.routeType = :type";

        List<RoutesDb> routesData = em.createQuery(routesHpql)
                .setParameter("from", ""+from)
                .setParameter("to", ""+to)
                .setParameter("type", routeType)
                .getResultList();

        String routesId = routesData
                .stream().map(RoutesDb::getTravelData)
                .findFirst()
                .orElse("");

        List<Line> lines = Arrays.stream(routesId
                .split(","))
                .filter(str -> !str.isEmpty())
                .map(strId -> em.find(LineDb.class, Long.parseLong(strId)))
                .map(Line::new)
                .collect(Collectors.toList());

        Route result = new Route();
        result.setDirect_paths(lines);
        result.setDuration_minutes(lines
                .stream()
                .mapToInt(Line::getDuration_minutes)
                .sum());
        result.setEuro_price(lines.stream().mapToDouble(Line::getEuro_price).sum());
        result.setRouteType(routeType);

        return result;
    }
*/

    @Transactional
    public String addLocation(Location location) {
        LocationDb locEntity = new LocationDb();
        locEntity.setName(location.getName());
        locEntity.setId(location.getId());
        String result = "Persisted successfully";
        try {
            em.persist(locEntity);
        } catch (EntityExistsException e) {
            result = "Location with id "+location.getId()+" already exist";
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    @Transactional
    public void addLine(LineReq line) {
        LineDb lineDb = new LineDb();
        lineDb.setDurationMinutes(line.getDuration_minutes());
        lineDb.setEuroPrice(line.getEuro_price());
        lineDb.setFrom(line.getFrom_id());
        lineDb.setTo(line.getTo_id());
        lineDb.setTransportationType(line.getTransportation_type());

        em.persist(lineDb);

    }

    @Transactional
    public void addRoute(long fromId, long toId, String routeType, List<LineDb> list) {
        RouteDb route = new RouteDb();
        route.setFrom(fromId);
        route.setTo(toId);
        route.setLines(list);
        route.setRouteType(routeType);

        em.persist(route);

    }

    public RouteDb getRouteDb(String routeType, long from, long to) {
        return em.find(RouteDb.class, new RouteKey(from, to, routeType));
    }

    public LineDb getLine(Long id) {
        return em.find(LineDb.class, id);
    }

    public void UpdateRoute(RouteDb routeDb, List<LineDb> lines) {
        routeDb.setLines(lines);
        em.flush();
    }

    public void deleteRoute(RouteDb routeDb) {
        em.remove(routeDb);
    }

    public List<LineDb> getAllLinesFrom(long locationId) {
        String linesJpql = "SELECT line FROM LineDb line WHERE line.from = :from";
        List<LineDb> result = em.createQuery(linesJpql)
                .setParameter("from", Long.valueOf(locationId))
                .getResultList();
        return result;
    }

    public LocationDb getLocation(long id) {
        return em.find(LocationDb.class, id);
    }
}
