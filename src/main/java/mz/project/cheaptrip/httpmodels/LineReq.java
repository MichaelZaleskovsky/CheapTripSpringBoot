package mz.project.cheaptrip.httpmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.project.cheaptrip.entities.LineDb;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineReq {
    private long id;
    private long transportType;
    private BigDecimal price;
    private long currencyId;
    private String line;
    private int duration_minutes;
    private long from_id;
    private long to_id;
}
