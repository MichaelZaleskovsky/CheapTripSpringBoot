package mz.project.cheaptrip.httpmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.project.cheaptrip.entities.LineDb;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineRes {
    private long id;
    private String transportation_type;
    private BigDecimal euro_price;
    private int duration_minutes;
    private String from;
    private String to;
    private long from_id;
    private long to_id;

    public LineRes(LineDb line) {
        this.transportation_type = line.getTransport().getName();
        BigDecimal rate = line.getCurrency().getOneEuroRate();
        if(rate.compareTo(new BigDecimal(0)) != 0)
            this.euro_price = line.getPrice().divide(rate, 2, RoundingMode.HALF_EVEN);
        else
            this.euro_price = new BigDecimal(0);
        this.duration_minutes = line.getTimeImMinutes();
        this.from_id = line.getFrom();
        this.to_id = line.getTo();
        this.id = line.getId();
        this.from = "";
        this.to = "";
    }
}
