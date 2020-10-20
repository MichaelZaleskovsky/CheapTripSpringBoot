package mz.project.cheaptrip.dao;

import mz.project.cheaptrip.entities.*;
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

        String locSql = "SELECT * FROM location_db WHERE id IN (SELECT `" + typeStr + "` FROM route_db) " +
                "AND `name` LIKE '%" + searchName + "%' " +
                "ORDER BY CASE WHEN name LIKE '" + searchName + "%' THEN 0 ELSE 1 END LIMIT " + limit;

        List<LocationDb> locations = em.createNativeQuery(locSql, LocationDb.class)
                .getResultList();

        return locations;
    }

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

        lineDb.setId(line.getId());

        lineDb.setTimeImMinutes(line.getDuration_minutes());
        lineDb.setPrice(line.getPrice());
        lineDb.setFrom(line.getFrom_id());
        lineDb.setTo(line.getTo_id());
        lineDb.setLine(line.getLine());
        TransportDb transport = em.find(TransportDb.class, line.getTransportType());
        CurrencyDb curr = em.find(CurrencyDb.class, line.getCurrencyId());

        em.persist(lineDb);

        lineDb.setTransport(transport);
        lineDb.setCurrency(curr);
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

    @Transactional
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

    public List<String> checkStatus(){
        List<String> list = em.createNativeQuery("SHOW TABLES")
                .getResultList();
        return list;
    }

    @Transactional
    public void addTransport(TransportDb transport) {
        em.persist(transport);
    }

    public List<TransportDb> getTransports() {
        String jpql = "SELECT t from TransportDb t";
        return em.createQuery(jpql).getResultList();
    }

    @Transactional
    public void addCurrency(CurrencyDb curr) {
        em.persist(curr);
    }

    public List<CurrencyDb> getCurrencies() {
        String jpql = "SELECT c from CurrencyDb c";
        return em.createQuery(jpql).getResultList();
    }
}
