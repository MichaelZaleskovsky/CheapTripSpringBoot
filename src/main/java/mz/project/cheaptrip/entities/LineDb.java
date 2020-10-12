package mz.project.cheaptrip.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class LineDb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "transportation_type")
    private String transportationType;

    @Column(name = "euro_price")
    private double euroPrice;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "`from`")
    private long from;

    @Column(name = "`to`")
    private long to;
}
