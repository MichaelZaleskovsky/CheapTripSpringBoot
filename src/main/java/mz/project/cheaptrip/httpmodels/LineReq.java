package mz.project.cheaptrip.httpmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.project.cheaptrip.entities.LineDb;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineReq {
    private String transportation_type;
    private double euro_price;
    private int duration_minutes;
    private long from_id;
    private long to_id;
}
