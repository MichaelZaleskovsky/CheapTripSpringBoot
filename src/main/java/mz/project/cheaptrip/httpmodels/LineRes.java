package mz.project.cheaptrip.httpmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.project.cheaptrip.entities.LineDb;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineRes {
    private long id;
    private String transportation_type;
    private double euro_price;
    private int duration_minutes;
    private String from;
    private String to;
    private long from_id;
    private long to_id;

    public LineRes(LineDb line) {
        this.transportation_type = line.getTransportationType();
        this.euro_price = line.getEuroPrice();
        this.duration_minutes = line.getDurationMinutes();
        this.from_id = line.getFrom();
        this.to_id = line.getTo();
        this.id = line.getId();
        this.from = "";
        this.to = "";
    }
}
