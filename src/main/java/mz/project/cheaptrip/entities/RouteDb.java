package mz.project.cheaptrip.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@IdClass(RouteKey.class)
public class RouteDb {

    @Id
    @Column(name = "`from`")
    private long from;

    @Id
    @Column(name = "`to`")
    private long to;

    @Id
    @Column(name = "route_type", length=50)
    private String routeType;

    @ManyToMany
    @OrderColumn(name="route_line")
    private List<LineDb> lines;
}
