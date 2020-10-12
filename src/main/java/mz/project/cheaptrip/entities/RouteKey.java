package mz.project.cheaptrip.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RouteKey implements Serializable {

    @Column(name = "`from`")
    private long from;

    @Column(name = "`to`")
    private long to;

    @Column(name = "route_type", length=50)
    private String routeType;
}
