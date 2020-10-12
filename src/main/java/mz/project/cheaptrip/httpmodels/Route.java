package mz.project.cheaptrip.httpmodels;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.project.cheaptrip.entities.LineDb;
import mz.project.cheaptrip.entities.RouteDb;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Route {
    private String routeType;
    private double euro_price;
    private int duration_minutes;
    private List<LineRes> direct_paths;

    public Route(RouteDb route) {
        this.routeType = route.getRouteType();
        this.euro_price = route.getLines().stream().mapToDouble(LineDb::getEuroPrice).sum();
        this.duration_minutes = route.getLines().stream().mapToInt(LineDb::getDurationMinutes).sum();
        this.direct_paths = route.getLines().stream().map(LineRes::new).collect(Collectors.toList());
    }
}
