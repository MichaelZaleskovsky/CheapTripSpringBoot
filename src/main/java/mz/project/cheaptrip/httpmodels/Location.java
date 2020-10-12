package mz.project.cheaptrip.httpmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mz.project.cheaptrip.entities.LocationDb;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private long id;
    private String name;

    public Location(LocationDb l) {
        this.id = l.getId();
        this.name = l.getName();
    }
}
