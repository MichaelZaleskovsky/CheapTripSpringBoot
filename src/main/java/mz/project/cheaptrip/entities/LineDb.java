package mz.project.cheaptrip.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class LineDb {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private TransportDb transport;

/*
    @Column(name = "euro_price")
    private double euroPrice;
*/

    @Column(scale=2)
    private BigDecimal price;

    @ManyToOne
    private CurrencyDb currency;

    private String line;

    private int timeImMinutes;

    @Column(name = "`from`")
    private long from;

    @Column(name = "`to`")
    private long to;
}
