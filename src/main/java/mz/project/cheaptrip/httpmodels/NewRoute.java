package mz.project.cheaptrip.httpmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewRoute {
    private long fromId;
    private long toId;
    private String routeType;
    private String travelData;
}
